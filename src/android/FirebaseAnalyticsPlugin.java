package by.chemerisuk.cordova.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import by.chemerisuk.cordova.support.CordovaMethod;
import by.chemerisuk.cordova.support.ReflectiveCordovaPlugin;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class FirebaseAnalyticsPlugin extends ReflectiveCordovaPlugin {
    private static final String TAG = "FirebaseAnalyticsPlugin";

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void pluginInitialize() {
        Log.d(TAG, "Starting Firebase Analytics plugin");
        Context context = this.cordova.getActivity().getApplicationContext();
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @CordovaMethod
    protected void logEvent(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String name = args.getString(0);
        JSONObject params = args.getJSONObject(1);
        firebaseAnalytics.logEvent(name, parse(params));
        callbackContext.success();
    }

    @CordovaMethod
    protected void setUserId(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String userId = args.getString(0);
        firebaseAnalytics.setUserId(userId);
        callbackContext.success();
    }

    @CordovaMethod
    protected void setUserProperty(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String name = args.getString(0);
        String value = args.getString(1);
        firebaseAnalytics.setUserProperty(name, value);
        callbackContext.success();
    }

    @CordovaMethod
    protected void resetAnalyticsData(CordovaArgs args, CallbackContext callbackContext) {
        firebaseAnalytics.resetAnalyticsData();
        callbackContext.success();
    }

    @CordovaMethod
    protected void setAnalyticsCollectionEnabled(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        boolean enabled = args.getBoolean(0);
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled);
        callbackContext.success();
    }

    @CordovaMethod
    protected void setEnabled(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        boolean enabled = args.getBoolean(0);
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled);
        callbackContext.success();
    }

    @CordovaMethod
    protected void setCurrentScreen(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String screenName = args.getString(0);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        callbackContext.success();
    }

    @CordovaMethod
    protected void setDefaultEventParameters(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        JSONObject params = args.getJSONObject(0);
        firebaseAnalytics.setDefaultEventParameters(parse(params));
        callbackContext.success();
    }

    @CordovaMethod
    protected void getSessionId(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        Task<Long> sessionIdTask = firebaseAnalytics.getSessionId();
        sessionIdTask.addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long sessionId) {
                if (sessionId != null) {
                    callbackContext.success(sessionId.toString());
                } else {
                    callbackContext.success("null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbackContext.error("Failed to retrieve session ID: " + e.getMessage());
            }
        });        
    }

    @CordovaMethod
    protected void getAppInstanceId(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        Task<String> appInstanceIdTask = firebaseAnalytics.getAppInstanceId();
        appInstanceIdTask.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String appInstanceId) {
                if (appInstanceId != null) {
                    callbackContext.success(appInstanceId.toString());
                } else {
                    callbackContext.success("null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbackContext.error("Failed to retrieve App Instance ID: " + e.getMessage());
            }
        });        
    }
    
    @CordovaMethod
    protected void setConsent(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        JSONObject consentSettings = args.getJSONObject(0);
        Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> consentMap = new HashMap<>();

        // Define valid consent types and statuses
        Map<String, FirebaseAnalytics.ConsentType> validConsentTypes = new HashMap<>();
        validConsentTypes.put("ANALYTICS_STORAGE", FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE);
        validConsentTypes.put("AD_STORAGE", FirebaseAnalytics.ConsentType.AD_STORAGE);
        validConsentTypes.put("AD_USER_DATA", FirebaseAnalytics.ConsentType.AD_USER_DATA);
        validConsentTypes.put("AD_PERSONALIZATION", FirebaseAnalytics.ConsentType.AD_PERSONALIZATION);

        Map<String, FirebaseAnalytics.ConsentStatus> validConsentStatuses = new HashMap<>();
        validConsentStatuses.put("GRANTED", FirebaseAnalytics.ConsentStatus.GRANTED);
        validConsentStatuses.put("DENIED", FirebaseAnalytics.ConsentStatus.DENIED);

        Iterator<String> keys = consentSettings.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String status = consentSettings.getString(key).toUpperCase();

            if (!validConsentTypes.containsKey(key.toUpperCase())) {
                callbackContext.error("Invalid consent type: " + key);
                return;
            }

            if (!validConsentStatuses.containsKey(status)) {
                callbackContext.error("Invalid consent status for type " + key + ": " + status);
                return;
            }

            FirebaseAnalytics.ConsentType consentType = validConsentTypes.get(key.toUpperCase());
            FirebaseAnalytics.ConsentStatus consentStatus = validConsentStatuses.get(status);

            consentMap.put(consentType, consentStatus);
        }

        firebaseAnalytics.setConsent(consentMap);
        callbackContext.success();
    }


    private static Bundle parse(JSONObject params) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator<String> it = params.keys();

        while (it.hasNext()) {
            String key = it.next();
            Object value = params.get(key);

            if (value instanceof String) {
                bundle.putString(key, (String)value);
            } else if (value instanceof Integer) {
                bundle.putInt(key, (Integer)value);
            } else if (value instanceof Double) {
                bundle.putDouble(key, (Double)value);
            } else if (value instanceof Long) {
                bundle.putLong(key, (Long)value);
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray)value;
                ArrayList<Bundle> items = new ArrayList<>();
                for (int i = 0, n = jsonArray.length(); i < n; i++) {
                    items.add(parse(jsonArray.getJSONObject(i)));
                }
                bundle.putParcelableArrayList(key, items);
            } else {
                Log.w(TAG, "Value for key " + key + " is not supported");
            }
        }

        return bundle;
    }
}
