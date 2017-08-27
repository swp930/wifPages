package com.aa.ndchtml5.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aa.ndchtml5.common.Feature;
import com.aa.ndchtml5.common.Stop;
import com.aa.ndchtml5.domain.Offers.Offer;
import com.aa.ndchtml5.web.model.OfferDetails;

public class ConvertDataToView {
	
	/**
	 * This method will return the offerDetails given the offer
	 * @param offer
	 * @return
	 */
	public static OfferDetails getOfferDetails(Offer offer) {
		if (offer == null) {
			return null;
		}
		OfferDetails offerDetails = new OfferDetails();
		
		offerDetails.setOfferId(offer.getOfferId());
		offerDetails.setOfferExpiryDate(populateExpiryDate(offer.getOfferExpiryTime()));
		offerDetails.setFlightDetails(getFlightDetails(offer));
		offerDetails.setFare("$" + offer.getPrice().getTotal());
		offerDetails.setIncludeFeatures(offer.getIncludedFeatures().getFeature());
		offerDetails.setPurchaseFeautues(offer.getForPurchaseFeatures().getFeature());
		offerDetails.setPrice(offer.getPrice());
		offerDetails.setFareRules(offer.getFareRules());
		offerDetails.setInclTotalFeatures(getTotalFeature(offerDetails.getIncludeFeatures()));
		offerDetails.setPurTotalFeautues(getTotalFeature(offerDetails.getPurchaseFeautues()));
		offerDetails.setNoOfSegments(offer.getSliceDetail().getNumberOfSegments());
		offerDetails.setSegmentDetailList(offer.getSegmentDetail());
		
		
		return offerDetails;
	}

	/**
	 * This method will return Flight itinerary string
	 * @param offer
	 * @return
	 */
	private static String getFlightDetails(Offer offer) {
		String stops = getStopDetails(offer.getSliceDetail().getNumberOfSegments());
		String flightDetails = stops + " " + offer.getSliceDetail().getOrigin() + "-" +  offer.getSliceDetail().getDestination();
		return flightDetails;
	}
	
	/**
	 * This method will return number of stops string
	 * @param stops
	 * @return
	 */
	private static String getStopDetails(int stops) {
		switch (stops) {
		case 0:
			return Stop.NONSTOP.getValue();
		case 1:
			return Stop.ONE.getValue();
		case 2:
			return Stop.TWO.getValue();
		case 3:
			return Stop.THREE.getValue();
		default:
			return null;
		}
		
	}
	
	/**
	 * This method will return formatted offer expiry date
	 * @param dateStr
	 * @return
	 */
	private static String populateExpiryDate(String dateStr) {
		if (dateStr == null)
			return null;
		dateStr = dateStr.replace('T', ' ');
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat stringFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
		String formattedStr = dateStr; 
		Date date;
		try {
			date = (Date)dateFormatter.parse(dateStr);
			formattedStr = (String)stringFormatter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		formattedStr = "(Expires " +formattedStr+")";
		return formattedStr;
	}
	
	private static List<Feature> getTotalFeature(List<String> features) {
		List<Feature> totalFeatures = new ArrayList<Feature>();
		for (String feature : features) {
				totalFeatures.add(Feature.getByCode(feature));
			}
		return totalFeatures;
		}
}
