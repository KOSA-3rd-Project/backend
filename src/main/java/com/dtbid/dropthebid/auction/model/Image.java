package com.dtbid.dropthebid.auction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
	int auctionImageId;
	int auctionId;
	String url;
	String extension;
	String mainImage;
	long imageSize;
}
