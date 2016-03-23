package com.zgld.mall.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.zgld.mall.SysApplication;
import com.zgld.mall.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览
 */
public class ImageBrowserAdapter extends PagerAdapter {

	private List<String> mPhotos = new ArrayList<String>();
	Context context;

	public ImageBrowserAdapter(Context context, List<String> photos) {
		mPhotos = photos;
		this.context = context;
	}

	@Override
	public int getCount() {
		if (mPhotos.size() > 1) {
			return Integer.MAX_VALUE;
		}
		return mPhotos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		// imageLoader.DisplayImageAbstract(
		// mPhotos.get(position % mPhotos.size()), photoView, false);
		SysApplication.DisplayAbstractImage(mPhotos.get(position % mPhotos.size()), photoView);
		container.addView(photoView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
