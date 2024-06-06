var PLUGIN_NAME = "FirebaseAnalytics";
// @ts-ignore
var exec = require("cordova/exec");

exports.logEvent =
/**
 * Logs an app event.
 *
 * @param {string} name Enent name
 * @param {Record<string, number | string | Array<object>>} params Event parameters
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @example
 * cordova.plugins.firebase.analytics.logEvent("my_event", {param1: "value1"});
 */
function(name, params) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "logEvent", [name, params || {}]);
    });
};

exports.setUserId =
/**
 * Sets the user ID property. This feature must be used in accordance with Google's Privacy Policy.
 *
 * @param {string} userId User's indentifier string
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @see https://www.google.com/policies/privacy
 *
 * @example
 * cordova.plugins.firebase.analytics.setUserId("12345");
 */
function(userId) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "setUserId", [userId]);
    });
};

exports.setUserProperty =
/**
 * Sets a user property to a given value. Be aware of automatically collected user properties.
 *
 * @param {string} name Property name
 * @param {string} value Property value
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @see https://support.google.com/firebase/answer/6317486?hl=en&ref_topic=6317484
 *
 * @example
 * cordova.plugins.firebase.analytics.setUserProperty("name1", "value1");
 */
function(name, value) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "setUserProperty", [name, value]);
    });
};

exports.resetAnalyticsData =
/**
 * Clears all analytics data for this instance from the device and resets the app instance ID.
 *
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @example
 * cordova.plugins.firebase.analytics.resetAnalyticsData();
 */
function() {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "resetAnalyticsData", []);
    });
};

exports.setEnabled =
/**
 * Sets whether analytics collection is enabled for this app on this device.
 *
 * @param {boolean} enabled Flag that specifies new state
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @example
 * cordova.plugins.firebase.analytics.setEnabled(false);
 */
function(enabled) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "setEnabled", [enabled]);
    });
};

exports.setCurrentScreen =
/**
 * Sets the current screen name, which specifies the current visual context in your app. This helps identify the areas in your app where users spend their time and how they interact with your app.
 *
 * @param {string} screenName Current screen name
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @example
 * cordova.plugins.firebase.analytics.setCurrentScreen("User dashboard");
 */
function(screenName) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "setCurrentScreen", [screenName]);
    });
};

exports.setDefaultEventParameters =
/**
 * Adds parameters that will be set on every event logged from the SDK, including automatic ones.
 * @param {Record<string, number | string | Array<object>>} defaults Key-value default parameters map
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @example
 * cordova.plugins.firebase.analytics.setDefaultEventParameters({foo: "bar"});
 */
function(defaults) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "setDefaultEventParameters", [defaults || {}]);
    });
};

exports.getSessionId =
/**
 * Gets the session Id Long
 *
 * @returns {Promise<void>} Returns the session id from the client. Returns null if ANALYTICS_STORAGE has been set to DENIED or session is expired.
 *
 * @example
 * cordova.plugins.firebase.analytics.getSessionId();
 */
function() {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "getSessionId", []);
    });
};


exports.getAppInstanceId =
/**
 * Gets the App Instance ID
 *
 * @returns {Promise<void>} Retrieves the app instance id from the service, or null if ANALYTICS_STORAGE has been set to DENIED.
 *
 * @example
 * cordova.plugins.firebase.analytics.getAppInstanceId();
 */
function() {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "getAppInstanceId", []);
    });
};

exports.setAnalyticsCollectionEnabled =
/**
 * Sets whether analytics collection is enabled for this app on this device. This setting is persisted across app sessions. By default it is enabled.
 *
 * @returns {Promise<void>} Callback when operation is completed
 *
 * @example
 * cordova.plugins.firebase.analytics.setAnalyticsCollectionEnabled();
 */
function() {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, PLUGIN_NAME, "setAnalyticsCollectionEnabled", []);
    });
};