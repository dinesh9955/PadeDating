package com.padedatingapp;

//import com.caffemoro.pay.AutoTopup;
//import com.caffemoro.util.ShowMsg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;


public class SavePref {
	
	 private static final String TAG = "SavePref";
	Context con;
	 String save_id;
	 
	 
	 SharedPreferences preferences = null;
	 Editor editor = null;
	 
	 
	 
	 public void clear()
	 {
//		 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//	    editor = preferences.edit();
	      editor.clear();
	      editor.commit();
		  
		  Log.d(TAG, "preferences cleared");
	 }
	
	public  void SavePref(Context c ){
		con = c;
		
		  preferences = PreferenceManager.getDefaultSharedPreferences(con);
		   editor = preferences.edit();
	}


	
	
	
	
		public void setText1(String string) {
			// TODO Auto-generated method stub
			
			  editor.putString("setText1",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setText1 received");
		}
		
		
		public String getText1() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setText1","");
			  
			  Log.d(TAG, "setText1 received "+name);
			  
			  return name;
		}


		
		
		
		
		public void setText2(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setText2",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setText2 received");
		}
		
		
		public String getText2() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setText2","");
			  
			  Log.d(TAG, "setText2 received "+name);
			  
			  return name;
		}

		
		
		
		
		public void setId(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setId",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setId received");
		}
		
		
		public String getId() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setId","");
			  
			  Log.d(TAG, "setId received "+name);
			  
			  return name;
		}




	public void setUserType(String string) {
		// TODO Auto-generated method stub
		editor.putString("setUserType",string);
		editor.commit();
		Log.d(TAG, "setUserType received");
	}


	public String getUserType() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setUserType","");
		Log.d(TAG, "setUserType received "+name);
		return name;
	}







	public void setName(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setName",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setName received");
		}
		
		
		public String getName() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setName","");
			  
			  Log.d(TAG, "setName received "+name);
			  
			  return name;
		}
		
		
		
	
		
		
		
		public void setEmail(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setEmail",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setEmail received");
		}
		
		
		public String getEmail() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setEmail","");
			  
			  Log.d(TAG, "setEmail received "+name);
			  
			  return name;
		}
		
		
		
		
		public void setPwd(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setPwd",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setPwd received");
		}
		
		
		public String getPwd() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setPwd","");
			  
			  Log.d(TAG, "setPwd received "+name);
			  
			  return name;
		}
		
		
		
		
		
		public void setUserName(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setUserName",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setUserName received");
		}
		
		
		public String gsetUserName() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setUserName","");
			  
			  Log.d(TAG, "setUserName received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		
		
		public void setLoginValue(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setLoginValue",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setLoginValue received");
		}
		
		
		public String getLoginValue() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setLoginValue","");
			  
			  Log.d(TAG, "setLoginValue received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		
		
		
		
		public void setLatLong(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setLatLong",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setLatLong received");
		}
		
		
		public String getLatLong() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setLatLong","");
			  
			  Log.d(TAG, "setLatLong received "+name);
			  
			  return name;
		}
		
		
		
		
		
		public void setCardNO(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCardNO",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCardNO received");
		}
		
		
		public String getCardNO() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCardNO","");
			  
			  Log.d(TAG, "setCardNO received "+name);
			  
			  return name;
		}

		
		
		
		
		
		public void setBalance(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setBalance",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setBalance received");
			  
			  
//			  if(getSwitchConfirm().equals("1")){
//				  Intent intent = new Intent();
//				  intent.setAction("MyBroadcast");
//				  intent.putExtra("value", 1000);
//				  con.sendBroadcast(intent);
//			  }else{
//				  
//			  }
			  
			  
			  

				
				
			  
			 
		}
		
		
		public String getBalance() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setBalance","0");
			  
			  Log.d(TAG, "setBalance received "+name);
			  
//			  if(getSwitchConfirm().equals("1")){
//				  Intent intent = new Intent();
//				  intent.setAction("MyBroadcast");
//				  
//				  double myBalance = Double.parseDouble(name);
//				  
//				  intent.putExtra("value", myBalance);
//				  con.sendBroadcast(intent);
//			  }else{
//				  
//			  }
			  
			  
			  
//				if(getSwitchConfirm().equals("1")){
//					
//					
//					
//					  Intent intent = new Intent();
//					  intent.setAction("MyBroadcast");
//					  
//					  double myBalance = Double.parseDouble(name);
//					  
//					  intent.putExtra("value", myBalance);
//					  con.sendBroadcast(intent);
//					  
//					  
//					 // new ShowMsg().createToast(AutoTopup.this, "Auto Top-up service started.");
//					  
//				  }else{
//					  
//				  }
				
				
			  
			  return name;
		}
		
		
		
		
		public void setFName(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setFName",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setFName received");
		}
		
		
		public String getFName() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setFName","");
			  
			  Log.d(TAG, "setFName received "+name);
			  
			  return name;
		}



	
		
		public void setLName(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setLName",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setLName received");
		}
		
		
		public String getLName() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setLName","");
			  
			  Log.d(TAG, "setLName received "+name);
			  
			  return name;
		}
		
		
		
		
		public void setAddress(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setAddress",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setAddress received");
		}
		
		
		public String getAddress() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setAddress","");
			  
			  Log.d(TAG, "setAddress received "+name);
			  
			  return name;
		}
		
		
		
		
		public void setCity(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCity",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCity received");
		}
		
		
		public String getCity() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCity","");
			  
			  Log.d(TAG, "setCity received "+name);
			  
			  return name;
		}


		
		
		public void setCountry(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCountry",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCountry received");
		}
		
		
		public String getCountry() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCountry","");
			  
			  Log.d(TAG, "setCountry received "+name);
			  
			  return name;
		}


		
		
		
		public void setPostcode(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setPostcode",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setPostcode received");
		}
		
		
		public String getPostcode() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setPostcode","");
			  
			  Log.d(TAG, "setPostcode received "+name);
			  
			  return name;
		}

		
		
		public void setPhone(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setPhone",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setPhone received");
		}
		
		
		public String getPhone() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setPhone","");
			  
			  Log.d(TAG, "setPhone received "+name);
			  
			  return name;
		}
		
		
		public void setKey(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setKey",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setKey received");
		}
		
		
		public String getKey() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setKey","-1");
			  
			  Log.d(TAG, "setKey received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		public void setSwitchConfirm(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("getSwitchConfirm",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "getSwitchConfirm received");
		}
		
		
		public String getSwitchConfirm() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("getSwitchConfirm","0");
			  
			  Log.d(TAG, "getSwitchConfirm received "+name);
			  
			  return name;
		}



		
		
		
		public void setBalanceFalls(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("getBalanceFalls",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "getBalanceFalls received");
		}
		
		
		public String getBalanceFalls() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("getBalanceFalls","0");
			  
			  Log.d(TAG, "getBalanceFalls received "+name);
			  
			  return name;
		}
	
		
		
		
		
		
		
		
		public void setBalanceTopup(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setBalanceTopup",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setBalanceTopup received");
		}
		
		
		public String getBalanceTopup() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setBalanceTopup","0");
			  
			  Log.d(TAG, "setBalanceTopup received "+name);
			  
			  return name;
		}
		
		
		
		
		
		public void setCardHolderName(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCardHolderName",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCardHolderName received");
		}
		
		
		public String getCardHolderName() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCardHolderName","0");
			  
			  Log.d(TAG, "setCardHolderName received "+name);
			  
			  return name;
		}

		
		
		
		
		
		
		
		
		
		public void setExpiry(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setExpiry",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setExpiry received");
		}
		
		
		public String getExpiry() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setExpiry","0");
			  
			  Log.d(TAG, "setExpiry received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		
		
		public void setCvv(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCvv",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCvv received");
		}
		
		
		public String getCvv() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCvv","0");
			  
			  Log.d(TAG, "setCvv received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		
		
		
		public void setCardType(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCardType",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCardType received");
		}
		
		
		public String getCardType() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCardType","0");
			  
			  Log.d(TAG, "setCardType received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		
		
		public void setDeviceId(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setDeviceId",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setDeviceId received");
		}
		
		
		public String getDeviceId() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setDeviceId","0");
			  
			  Log.d(TAG, "setDeviceId received "+name);
			  
			  return name;
		}

		
		
		
		public void setDeviceToken(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setDeviceToken",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setDeviceToken received");
		}
		
		
		public String getDeviceToken() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setDeviceToken","0");
			  
			  Log.d(TAG, "setDeviceToken received "+name);
			  
			  return name;
		}
		


	
		
		
		public void setStar(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setStar",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setStar received");
		}
		
		
		public String getStar() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setStar","0");
			  
			  Log.d(TAG, "setStar received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		
		
		public void setLevel(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setLevel",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setLevel received");
		}
		
		
		public String getLevel() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setLevel","0");
			  
			  Log.d(TAG, "setLevel received "+name);
			  
			  return name;
		}
		
		
		
		
		
		public void setUpdate(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setUpdate",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setUpdate received");
		}
		
		
		public String getUpdate() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setUpdate","0");
			  
			  Log.d(TAG, "setUpdate received "+name);
			  
			  return name;
		}

		
		
		
		
		
		public void setNoti(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setNoti",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setNoti received");
		}
		
		
		public String getNoti() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setNoti","0");
			  
			  Log.d(TAG, "setNoti received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		public void setTempCardNO(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setTempCardNO",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setTempCardNO received");
		}
		
		
		public String getTempCardNO() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setTempCardNO","0");
			  
			  Log.d(TAG, "setTempCardNO received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		

		public void setTipValue(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setTipValue",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setTipValue received");
		}
		
		
		public String getTipValue() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setTipValue","0");
			  
			  Log.d(TAG, "setTipValue received "+name);
			  
			  return name;
		}


		
		
		public void setTipAmount(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setTipAmount",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setTipAmount received");
		}
		
		
		public String getTipAmount() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setTipAmount","0");
			  
			  Log.d(TAG, "setTipAmount received "+name);
			  
			  return name;
		}

		
		
		
		
		
		public void setCurrency(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCurrency",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCurrency received");
		}
		
		
		public String getCurrency() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCurrency","0");
			  
			  Log.d(TAG, "setCurrency received "+name);
			  
			  return name;
		}
		
		
		
		
		
		public void setCurrencyType(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setCurrencyType",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setCurrencyType received");
		}
		
		
		public String getCurrencyType() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setCurrencyType","0");
			  
			  Log.d(TAG, "setCurrencyType received "+name);
			  
			  return name;
		}


		
		
		
		
		
		public void setLatitude(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setLatitude",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setLatitude received");
		}
		
		
		public String getLatitude() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setLatitude","0");
			  
			  Log.d(TAG, "setLatitude received "+name);
			  
			  return name;
		}


		public void setLongitude(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setLongitude",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  
			  Log.d(TAG, "setLongitude received");
		}
		
		
		public String getLongitude() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setLongitude","0");
			  
			  Log.d(TAG, "setLongitude received "+name);
			  
			  return name;
		}
		
		
		
		
		
		
		public void setImage(String string) {
			// TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
			  editor.putString("setImage",string);
			  editor.commit();
			//  System.out.println("val save email ");
			  Log.d(TAG, "setImage received");
		}
		
		
		public String getImage() {
			// TODO Auto-generated method stub
		//	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
			  String name = preferences.getString("setImage","http://icons.iconarchive.com/icons/custom-icon-design/pretty-office-7/128/Save-icon.png");
			  Log.d(TAG, "setImage received "+name);
			  return name;
		}
		
		
		
		
		
		

		public void setTransactionId(String string) {
			// TODO Auto-generated method stub
			  editor.putString("setTransactionId",string);
			  editor.commit();
			  Log.d(TAG, "setTransactionId received");
		}
		
		
		public String getTransactionId() {
			// TODO Auto-generated method stub
			  String name = preferences.getString("setTransactionId","0");
			  Log.d(TAG, "setTransactionId received "+name);
			  return name;
		}





	public void setSwitch(String string) {
		// TODO Auto-generated method stub
		editor.putString("getSwitch",string);
		editor.commit();
		Log.d(TAG, "getSwitch received");
	}


	public String getSwitch() {
		// TODO Auto-generated method stub
		String name = preferences.getString("getSwitch","landlord");
		Log.d(TAG, "getSwitch received "+name);
		return name;
	}


	public void setTheme(String string) {
		// TODO Auto-generated method stub
		editor.putString("getTheme",string);
		editor.commit();
		Log.d(TAG, "getTheme received");
	}


	public String getTheme() {
		// TODO Auto-generated method stub
		String name = preferences.getString("getTheme","red");
		Log.d(TAG, "getTheme received "+name);
		return name;
	}




	public void setPropertyType(String string) {
		// TODO Auto-generated method stub
		editor.putString("setPropertyType",string);
		editor.commit();
		Log.d(TAG, "setPropertyType received");
	}


	public String getPropertyType() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setPropertyType","");
		Log.d(TAG, "setPropertyType received "+name);
		return name;
	}





	public void setHiddenAds(String string) {
		// TODO Auto-generated method stub
		editor.putString("setHiddenAds",string);
		editor.commit();
		Log.d(TAG, "setHiddenAds received");
	}


	public String getHiddenAds() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setHiddenAds","");
		Log.d(TAG, "setHiddenAds received "+name);
		return name;
	}




	public void setPropertyClass(String string) {
		// TODO Auto-generated method stub
		editor.putString("setPropertyClass",string);
		editor.commit();
		Log.d(TAG, "setPropertyClass received");
	}


	public String getPropertyClass() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setPropertyClass","");
		Log.d(TAG, "setPropertyClass received "+name);
		return name;
	}






	public void setLanguageId(String string) {
		// TODO Auto-generated method stub
		editor.putString("setLanguageId",string);
		editor.commit();
		Log.d(TAG, "setLanguageId received");
	}


	public String getLanguageId() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setLanguageId","0");
		Log.d(TAG, "setLanguageId received "+name);
		return name;
	}



	public void setOnline(String string) {
		// TODO Auto-generated method stub
		editor.putString("setOnline",string);
		editor.commit();
		Log.d(TAG, "setOnline received");
	}


	public String getOnline() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setOnline","0");
		Log.d(TAG, "setOnline received "+name);
		return name;
	}


	public void setTemplate(int string) {
		// TODO Auto-generated method stub
		editor.putInt("setTemplate",string);
		editor.commit();
		Log.d(TAG, "setTemplate received");
	}


	public int getTemplate() {
		// TODO Auto-generated method stub
		int name = preferences.getInt("setTemplate",0);
		Log.d(TAG, "setTemplate received "+name);
		return name;
	}


	public void setLanguagePosition(int string) {
		// TODO Auto-generated method stub
		editor.putInt("setLanguagePosition",string);
		editor.commit();
		Log.d(TAG, "setLanguagePosition received");
	}


	public int getLanguagePosition() {
		// TODO Auto-generated method stub
		int name = preferences.getInt("setLanguagePosition",0);
		Log.d(TAG, "setLanguagePosition received "+name);
		return name;
	}


	public void setNumberFormatPosition(int string) {
		// TODO Auto-generated method stub
		editor.putInt("getNumberFormatPosition",string);
		editor.commit();
		Log.d(TAG, "getNumberFormatPosition received");
	}


	public int getNumberFormatPosition() {
		// TODO Auto-generated method stub
		int name = preferences.getInt("getNumberFormatPosition",0);
		Log.d(TAG, "getNumberFormatPosition received "+name);
		return name;
	}


	public void setCustomerName(String string) {
		// TODO Auto-generated method stub
		editor.putString("setCustomerName",string);
		editor.commit();
		Log.d(TAG, "setCustomerName received");
	}


	public String getCustomerName() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setCustomerName","");
		Log.d(TAG, "setCustomerName received "+name);
		return name;
	}




	public void setSignIn(boolean string) {
		// TODO Auto-generated method stub
		editor.putBoolean("setSignIn",string);
		editor.commit();
		Log.d(TAG, "setSignIn received");
	}


	public boolean getSignIn() {
		// TODO Auto-generated method stub
		boolean name = preferences.getBoolean("setSignIn",false);
		Log.d(TAG, "setSignIn received "+name);
		return name;
	}



	public void setUser(String string) {
		// TODO Auto-generated method stub
		editor.putString("setUser",string);
		editor.commit();
		Log.d(TAG, "setUser received");
	}


	public String getUser() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setUser","");
		Log.d(TAG, "setUser received "+name);
		return name;
	}


	public void setCompany(String string) {
		// TODO Auto-generated method stub
		editor.putString("setCompany",string);
		editor.commit();
		Log.d(TAG, "setCompany received");
	}


	public String getCompany() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setCompany","");
		Log.d(TAG, "setCompany received "+name);
		return name;
	}


	public void setProduct(String string) {
		// TODO Auto-generated method stub
		editor.putString("setProduct",string);
		editor.commit();
		Log.d(TAG, "setProduct received");
	}


	public String getProduct() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setProduct","");
		Log.d(TAG, "setProduct received "+name);
		return name;
	}



	public void setItem(String string) {
		// TODO Auto-generated method stub
		editor.putString("setItem",string);
		editor.commit();
		Log.d(TAG, "setItem received");
	}


	public String getItem() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setItem","");
		Log.d(TAG, "setItem received "+name);
		return name;
	}



	public void setSubsType(String string) {
		// TODO Auto-generated method stub
		editor.putString("setSubsType",string);
		editor.commit();
		Log.d(TAG, "setSubsType received");
	}


	public String getSubsType() {
		// TODO Auto-generated method stub
		String name = preferences.getString("setSubsType","");
		Log.d(TAG, "setSubsType received "+name);
		return name;
	}

}
