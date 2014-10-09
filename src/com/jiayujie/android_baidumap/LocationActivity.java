package com.jiayujie.android_baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

public class LocationActivity extends Activity {

	private MapView mapView;
	private LocationClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		/**
		 * 验证密钥,
		 * 传入当前的Application：
		 * 注册有name为com.baidu.lbsapi.API_KEY 的 元数据
		 * 百度地图内部将取出对应的value去后台比较是否正确
		 */
		SDKInitializer.initialize(getApplicationContext());//验证密钥

		//加载一个含有MapView的布局文件
		setContentView(R.layout.activity_route);
		
		//获取地图控件对象
		mapView = (MapView)this.findViewById(R.id.bmapView);
		
		//获取位置
		getMyLocation();
	}

	private void getMyLocation() {
		// TODO 定位服务
		//初始化接收位置信息的客户端
		client = new LocationClient(getApplicationContext());
		//设置定位客户端参数
		LocationClientOption option=new LocationClientOption();
		option.setCoorType("bd09ll");//设置坐标类型
		option.setTimeOut(30000);
		option.setScanSpan(3000);	//设置扫描间隔
		/**
		 * 设置定位模式
		 * 1、高精度  LocationMode.Hight_Accuracy
		 * 2、低功耗  LocationMode.Battery_Saving
		 * 3、仅设备  LocationMode.Device_Sensors
		 */
		option.setLocationMode(LocationMode.Hight_Accuracy);
		
		option.setIsNeedAddress(true);//设置是否需要地址信息
		
		client.setLocOption(option);//将设置好的参数对象设置到客户端上
		
		//客户端添加监听器
		client.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				// TODO 当接收到位置信息是被触发
				/**
				 * @author jiayujie
				 * 1、获取位置
				 * 2、用经纬度设置一个点到地图上
				 */
				Log.i("", "======"+arg0.getLatitude()+","+arg0.getLongitude());
//				arg0.getLatitude();
				LatLng latLng=new LatLng(arg0.getLatitude(), arg0.getLongitude());
				
			}
		});
		//开启客户端
		client.start();
		//开启定位
		if (client.isStarted()&&client!=null) {
			client.requestLocation();
		}
	}
}
