package com.jiayujie.android_baidumap;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class MainActivity extends Activity implements OnGetPoiSearchResultListener {
	
	private MapView mapView;
	private  BaiduMap baiduMap;//地图对象的控制器
	private PoiSearch poiSearch;
	private WebView webView;//定义一个webview显示详情

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 验证密钥,
		 * 传入当前的Application：
		 * 注册有name为com.baidu.lbsapi.API_KEY 的 元数据
		 * 百度地图内部将取出对应的value去后台比较是否正确
		 */
		SDKInitializer.initialize(getApplicationContext());//验证密钥

		//加载一个含有MapView的布局文件
		setContentView(R.layout.activity_main);
		
		//获取地图控件对象
		mapView = (MapView)this.findViewById(R.id.bmapView);
		webView=(WebView)this.findViewById(R.id.webView);
		/**
		 * 设置mapView的属性
		 * 1、获得mapView的控制器
		 * 2、设置属性
		 */
		baiduMap=mapView.getMap();//获取控制器
		
		baiduMap.setTrafficEnabled(true);//设置是否开启交通路况
		
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL );//设置地图模式，有两种，普通图(MAP_TYPE_NORMAL)和卫星图(MAP_TYPE_SATELLITE )
		
		baiduMap.setMaxAndMinZoomLevel(19, 12);//设置缩放级别，手机级别限制为[3,19],级别越大越精确，大等级在前，小等级在后
		
		
		setOnePointToMap();//在地图上显示一个点
		setPointListener();//设置点的点击事件
		
		/**
		 * 获取兴趣点，并显示在地图上
		 */
		poiSearch=PoiSearch.newInstance();//单例模式，申请POI对象
		poiSearch.setOnGetPoiSearchResultListener(this);//添加监听器
		poiSearch.searchInCity(new PoiCitySearchOption().
				city("北京").
				keyword("美食").
				pageNum(0).
				pageCapacity(10));
	}

	private void setPointListener() {
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				/**
				 * 设置弹窗
				 * @author jiayujie
				 * @param View
				 * @param 位置
				 * @param Y轴偏移量(-70)
				 * @param 点击弹窗监听器
				 * BitmapDescriptor 是将所有的控件合并成一个控件，只有一个点击事件
				 */
				Button btn= new Button(getApplicationContext());//生成一个button对象，并设置其属性
				btn.setBackgroundColor(Color.GRAY);
				btn.setText(arg0.getTitle());
				BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromView(btn);//将button变成一张平面的图片
				InfoWindow infoWindow=new InfoWindow(bitmapDescriptor, arg0.getPosition(), -50,new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick() {
						// TODO 当用户点击弹窗时触发
						Log.i("", "======点击了 ：beijing_zoo");
						baiduMap.hideInfoWindow();//隐藏弹窗
					}
				});
				baiduMap.showInfoWindow(infoWindow);//显示弹窗
				return false;
			}
		});
	}
	private void setOnePointToMap() {
		// TODO 在地图上设置一个点
		/**
		 * 1、创建一个覆盖层属性的对象
		 * 2、设置属性
		 * 3、创建图片描述器
		 * 4、设置位置
		 */
		BitmapDescriptor fromResource = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);//图片描述器,工厂模式
		
		LatLng latLng=new LatLng(39.94791, 116.343615);// 创建经纬度对象(纬度，经度)
		
		//创建覆盖层属性对象
		OverlayOptions options=new MarkerOptions().
				title("beijing_zoo").
				icon(fromResource).
				position(latLng);
		baiduMap.addOverlay(options);//将设置好数据的覆盖层添加到地图上
	}
	
	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
	        mapView.onDestroy();  
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        mapView.onResume();  
	        }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        mapView.onPause();  
	        }

	    //POI检索事件
	    /**
	     * 1.获取到Poi详情
	     * 2.获取到Poi结果
	     */
		@Override
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
			// TODO 获取到Poi详情，当获取到PUI详情的时候触发
			String url = arg0.getDetailUrl();
			
			Log.i("", "======成功");
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setScrollBarStyle(0);
			WebSettings webSettings=webView.getSettings();
			webSettings.setAllowFileAccess(true);
			webSettings.setBuiltInZoomControls(true);
			webView.setWebViewClient(new WebViewClient(){
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO 自动生成的方法存根
					return false;
				}
			});
			webView.loadUrl(arg0.getDetailUrl());
		}

		@Override
		public void onGetPoiResult(PoiResult arg0) {
			// TODO 获取到Poi结果
//			PoiOverlay poiOverlay=new PoiOverlay(baiduMap);
			MyPoiOverlay poiOverlay=new MyPoiOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(poiOverlay);
			poiOverlay.setData(arg0);
			poiOverlay.addToMap();
//			poiOverlay.zoomToSpan();//将地图缩放至可以显示全部兴趣点（适当）的级别
		
		}  
	   class  MyPoiOverlay extends PoiOverlay{

		public MyPoiOverlay(BaiduMap arg0) {
			super(arg0);
			// TODO 自动生成的构造函数存根
		} 
		   
		@Override
		public boolean onPoiClick(int arg0) {
			// TODO 当前覆盖层中的POI被点击时触发
			List<PoiInfo> lists = getPoiResult().getAllPoi();
			PoiInfo poiInfo = lists.get(arg0);
//			Toast.makeText(MainActivity.this, "当前点击的兴趣点为： "+poiInfo.name, Toast.LENGTH_SHORT).show();
			//设置弹窗，内容为poiInfo.name
			
			Button btn= new Button(getApplicationContext());//生成一个button对象，并设置其属性
			btn.setText(poiInfo.name);
			btn.setTextSize(12);
			btn.setBackgroundColor(Color.GRAY);
			BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromView(btn);//将button变成一张平面的图片
			InfoWindow infoWindow=new InfoWindow(bitmapDescriptor, poiInfo.location, -50,new OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick() {
					// TODO 当用户点击弹窗时触发
					baiduMap.hideInfoWindow();//隐藏弹窗
				}
			});
			baiduMap.showInfoWindow(infoWindow);//显示弹窗
			
			/**
			 * 二次检索索引点详情，通过（UID）
			 */
			poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));
			
			return super.onPoiClick(arg0);
		}
	   }
}
