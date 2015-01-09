/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 *
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 *
 * AndTinder is compatible with API Level 13 and upwards
 *
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package com.andtinder.model;

public class CardModel {

	private String   title;
	private String   description;
	private String cardImageDrawable;
	private String cardLikeImageDrawable;
	private String cardDislikeImageDrawable;

    private OnCardDimissedListener mOnCardDimissedListener = null;

    private OnClickListener mOnClickListener = null;

    public interface OnCardDimissedListener {
        void onLike();
        void onDislike();
    }

    public interface OnClickListener {
        void OnClickListener();
    }

	public CardModel() {
		this(null, null, null);
	}

	/*public CardModel(String title, String description, String string) {
		this.title = title;
		this.description = description;
		this.cardImageDrawable = string;
	}*/

	public CardModel(String title, String description, String cardImage) {
		this.title = title;
		this.description = description;
		try {
			this.cardImageDrawable =  cardImage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCardImageDrawable() {
		return cardImageDrawable;
	}

	public void setCardImageDrawable(String cardImageDrawable) {
		this.cardImageDrawable = cardImageDrawable;
	}

	public String getCardLikeImageDrawable() {
		return cardLikeImageDrawable;
	}

	public void setCardLikeImageDrawable(String cardLikeImageDrawable) {
		this.cardLikeImageDrawable = cardLikeImageDrawable;
	}

	public String getCardDislikeImageDrawable() {
		return cardDislikeImageDrawable;
	}

	public void setCardDislikeImageDrawable(String cardDislikeImageDrawable) {
		this.cardDislikeImageDrawable = cardDislikeImageDrawable;
	}

    public void setOnCardDimissedListener( OnCardDimissedListener listener ) {
        this.mOnCardDimissedListener = listener;
    }

    public OnCardDimissedListener getOnCardDimissedListener() {
       return this.mOnCardDimissedListener;
    }


    public void setOnClickListener( OnClickListener listener ) {
        this.mOnClickListener = listener;
    }

    public OnClickListener getOnClickListener() {
        return this.mOnClickListener;
    }
}