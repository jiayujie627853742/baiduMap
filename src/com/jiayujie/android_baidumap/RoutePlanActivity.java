package com.jiayujie.android_baidumap;

import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class RoutePlanActivity extends Activity implements OnGetRoutePlanResultListener {

	
	private MapView mapView;
	private BaiduMap baiduMap;
	
	//路线规划检索器
	private RoutePlanSearch routePlanSearch;
	private DrivingRouteLine drivingRouteLine;
	private MyRouteOverLay drivingRouteOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getApplicationContext());//验证密钥

		//加载一个含有MapView的布局文件
		setContentView(R.layout.activity_route);
		
		//获取地图控件对象
		mapView = (MapView)this.findViewById(R.id.amapView);
		/**
		 * 设置mapView的属性
		 * 1、获得mapView的控制器
		 * 2、设置属性
		 */
		baiduMap=mapView.getMap();//获取控制器
		
		baiduMap.setTrafficEnabled(true);//设置是否开启交通路况
		
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL );//设置地图模式，有两种，普通图(MAP_TYPE_NORMAL)和卫星图(MAP_TYPE_SATELLITE )
		
		baiduMap.setMaxAndMinZoomLevel(19, 12);//设置缩放级别，手机级别限制为[3,19],级别越大越精确，大等级在前，小等级在后

		
		
		//初始化路线规划检索器
		routePlanSearch=RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(this);
		routePlanSearch.drivingSearch(new  DrivingRoutePlanOption().from(PlanNode.withCityNameAndPlaceName("北京", "宝盛里")).to(PlanNode.withCityNameAndPlaceName("北京", "清河")));
		
	}
	
	/**
	 * @author jiayujie
	 * 路线规划监听器方法
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
		drivingRouteOverlay = new MyRouteOverLay(baiduMap);
		
		/**
		 * 1、通过getRouteLines获取到一个包含路线方案的集合
		 * 2、获取集合中的一条路线
		 * 3、将路线设置到图层上
		 * 4、图层设置到地图上
		 */
		List<DrivingRouteLine>lists=arg0.getRouteLines();//1
		drivingRouteLine = lists.get(0);//2
		drivingRouteOverlay.setData(drivingRouteLine);//3
		baiduMap.setOnMarkerClickListener(drivingRouteOverlay);
		drivingRouteOverlay.addToMap();//4
	}
	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO 获取到公交路线时触发
		
	}
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO 当获取到不行路线时触发
		
	}
	
	class MyRouteOverLay extends DrivingRouteOverlay{

		public MyRouteOverLay(BaiduMap arg0) {
			super(arg0);
			// TODO 自动生成的构造函数存根
		}
		@Override
		public boolean onRouteNodeClick(int position) {
			// TODO 当点击了当前覆盖层上线路点时触发
			
			List<DrivingStep> lists = drivingRouteLine.getAllStep();//通过当前路线调用其getAllStep方法，可以获取到包含当前路线中所有节段的集合
			DrivingStep step = lists.get(position);
			
			Log.i("", "======"+step.getInstructions());
			
			return super.onRouteNodeClick(position);
		}
	}
}
