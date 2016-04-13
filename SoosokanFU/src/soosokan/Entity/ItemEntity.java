package soosokan.Entity;

import java.sql.Timestamp;


	public class ItemEntity{
		private String _id;
		private String sellerId;
		private String name;
		private String keyword;
		private float price;
		private Timestamp time;
		private String picId;
		private int discount;
		
		/**
		 * To generate a item entity
		 * @param itemId is the identify of the item
		 * @param sellerId is the identify of the seller who post the item
		 * @param name the item name
		 * @param keyword to describe the item
		 * @param price the price of the item
		 * @param time timestamp
		 * @param discount is the item a discount item
		 * @param filePath the path of the item picture
		 */
		public ItemEntity(String itemId, String sellerId, String name,
				String keyword, float price, Timestamp time, int discount, String filePath) {
			super();
			this._id = itemId;
			this.sellerId = sellerId;
			this.name = name;
			this.keyword = keyword;
			this.price = price;
			this.time = time;
			this.discount = discount;
			//this.picId = PictureHandler.savePic(filePath, _id);
			this.picId = "000";
		}
		
		public int isDiscount() {
			return discount;
		}

		public void setDiscount(int discount) {
			this.discount = discount;
		}

		public String getPicId() {
			return picId;
		}
		public void setPicId(String picId) {
			this.picId = picId;
		}
		public String getItemId() {
			return _id;
		}
		public void setItemId(String itemId) {
			this._id = itemId;
		}
		public String getSellerId() {
			return sellerId;
		}
		public void setSellerId(String sellerId) {
			this.sellerId = sellerId;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
		public Timestamp getTime() {
			return time;
		}
		public void setTime(Timestamp time) {
			this.time = time;
		}
		@Override
		public String toString() {
			return "Item [_id=" + _id + ", sellerId=" + sellerId + ", name="
					+ name + ", keyword=" + keyword + ", price=" + price
					+ ", time=" + time + ", picId=" + picId + ", discount="
					+ discount + "]";
		}
		
		
		
	}