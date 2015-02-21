package com.adobe.summit.commerce.custom.rest.sdk.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.adobe.cq.commerce.api.promotion.VoucherInfo;
import com.adobe.summit.commerce.custom.rest.sdk.internal.OrderViewService;
import com.adobe.summit.commerce.custom.views.Order;
import com.elasticpath.rest.client.CortexClient;
import com.elasticpath.rest.client.jaxrs.JaxRsUtil;
import com.elasticpath.rest.sdk.components.AppliedPromotions;
import com.elasticpath.rest.sdk.components.Coupon;
import com.elasticpath.rest.sdk.components.Promotion;
import com.elasticpath.rest.sdk.service.PromotionService;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Predicate;
/**
 * Promotion service.
 */
public class PromotionServiceImpl implements PromotionService {

	private final CortexClient cortexClient;
	private final OrderViewService orderService;

	/**
	 * Creates a promotion service.
	 * @param cortexClient The cortex client.
	 * @param orderService The order service is used to get order data.
	 */
	public PromotionServiceImpl(final CortexClient cortexClient, final OrderViewService orderService) {
		this.cortexClient = cortexClient;
		this.orderService = orderService;
	}

	@Override
	public List<VoucherInfo> getVoucherInfos(final Locale userLocale) {
		final List<VoucherInfo> voucherInfoList = new ArrayList<VoucherInfo>();
		Order order = orderService.getOrder(userLocale);
		Iterable<Coupon> coupons = order.getCoupons();
		if (coupons != null) {
			for (Coupon coupon : coupons) {
				voucherInfoList.add(
						new VoucherInfo(
								coupon.getCode(),
								coupon.getSelf().getUri(),
								null, // title
								StringUtils.join(getPromotionDescriptions(coupon), ","),
								!coupon.getCouponTriggeredPromotions().isEmpty(),  //isValid
								StringUtils.join(getPromotionNames(coupon), ",")));
			}
		}
		return voucherInfoList;
	}

	@Override
	public boolean addCoupon(final String couponCode, final Locale userLocale) {
		Order order = orderService.getOrder(userLocale);
		Response.StatusType status = cortexClient.post("/coupons" + StringUtils.substringBefore(order.getSelf().getUri(), "?zoom="),
				ImmutableMap.of("code", couponCode));

		return JaxRsUtil.isSuccessful(status);
	}

	@Override
	public boolean deleteCoupon(final String couponCode, final Locale userLocale) {
		Order order = orderService.getOrder(userLocale);
		Optional<Coupon> coupon = filterCoupons(couponCode, order.getCoupons());

		if (coupon.isPresent()) {
			Response.StatusType status = cortexClient.delete(coupon.get().getSelf().getUri());

			return JaxRsUtil.isSuccessful(status);
		}

		return false;
	}

	private Optional<Coupon> filterCoupons(final String couponCode, final Iterable<Coupon> coupons) {
		return FluentIterable.from(coupons)
				.filter(new Predicate<Coupon>() {
					@Override
					public boolean apply(final Coupon coupon) {
						if (coupon != null) {
							return coupon.getCode().equals(couponCode);
						}

						return false;
					}
				})
				.first();
	}

	private List<String> getPromotionDescriptions(final Coupon coupon) {
		List<String> promoDescriptions = new ArrayList<String>();
		for (AppliedPromotions appliedPromotion : coupon.getCouponTriggeredPromotions()) {
			for (Promotion promotion : appliedPromotion.getPromotions()) {
				promoDescriptions.add(promotion.getDisplayDescription());
			}
		}
		return promoDescriptions;
	}

	private List<String> getPromotionNames(final Coupon coupon) {
		List<String> promoNames = new ArrayList<String>();
		for (AppliedPromotions appliedPromotion : coupon.getCouponTriggeredPromotions()) {
			for (Promotion promotion : appliedPromotion.getPromotions()) {
				promoNames.add(promotion.getDisplayName());
			}
		}
		return promoNames;
	}
}
