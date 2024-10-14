#import "FirebaseAnalyticsPlugin.h"

@import FirebaseCore;
@import FirebaseAnalytics;

@implementation FirebaseAnalyticsPlugin

- (void)pluginInitialize {
    NSLog(@"Starting Firebase Analytics plugin");

    if(![FIRApp defaultApp]) {
        [FIRApp configure];
    }
}

- (void)logEvent:(CDVInvokedUrlCommand *)command {
    NSString* name = [command.arguments objectAtIndex:0];
    NSDictionary* parameters = [command.arguments objectAtIndex:1];

    [FIRAnalytics logEventWithName:name parameters:parameters];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setUserId:(CDVInvokedUrlCommand *)command {
    NSString* id = [command.arguments objectAtIndex:0];

    [FIRAnalytics setUserID:id];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setUserProperty:(CDVInvokedUrlCommand *)command {
    NSString* name = [command.arguments objectAtIndex:0];
    NSString* value = [command.arguments objectAtIndex:1];

    [FIRAnalytics setUserPropertyString:value forName:name];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setEnabled:(CDVInvokedUrlCommand *)command {
    bool enabled = [[command.arguments objectAtIndex:0] boolValue];

    [FIRAnalytics setAnalyticsCollectionEnabled:enabled];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setCurrentScreen:(CDVInvokedUrlCommand *)command {
    NSString* screenName = [command.arguments objectAtIndex:0];

    [FIRAnalytics logEventWithName:kFIREventScreenView parameters:@{
        kFIRParameterScreenName: screenName
    }];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)resetAnalyticsData:(CDVInvokedUrlCommand *)command {
    [FIRAnalytics resetAnalyticsData];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setDefaultEventParameters:(CDVInvokedUrlCommand *)command {
    NSDictionary* params = [command.arguments objectAtIndex:0];

    [FIRAnalytics setDefaultEventParameters:params];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getSessionId:(CDVInvokedUrlCommand *)command {
    NSString *sessionId = [FIRAnalytics sessionID];

    CDVPluginResult *pluginResult;
    if (sessionId != nil) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:sessionId];
    } else {
        // Return null if session ID is not available
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:nil];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getAppInstanceId:(CDVInvokedUrlCommand *)command {
    NSString *appInstanceId = [FIRAnalytics getAppInstanceId];

    CDVPluginResult *pluginResult;
    if (appInstanceId != nil) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:appInstanceId];
    } else {
        // Return null if session ID is not available
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:nil];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (void)setConsent:(CDVInvokedUrlCommand *)command {
    NSDictionary *consentSettings = [command.arguments objectAtIndex:0];
    NSMutableDictionary<FIRConsentType, FIRConsentStatus> *consentMap = [NSMutableDictionary dictionary];

    // Define valid consent types and statuses
    NSDictionary<NSString *, NSNumber *> *validConsentTypes = @{
        @"ANALYTICS_STORAGE": @((NSInteger)FIRConsentTypeAnalyticsStorage),
        @"AD_STORAGE": @((NSInteger)FIRConsentTypeAdStorage),
        @"AD_USER_DATA": @((NSInteger)FIRConsentTypeAdUserData),          // New valid type
        @"AD_PERSONALIZATION": @((NSInteger)FIRConsentTypeAdPersonalization)  // New valid type
    };

    NSDictionary<NSString *, NSNumber *> *validConsentStatuses = @{
        @"GRANTED": @((NSInteger)FIRConsentStatusGranted),
        @"DENIED": @((NSInteger)FIRConsentStatusDenied)
    };

    for (NSString *key in consentSettings) {
        NSString *status = [consentSettings[key] uppercaseString];

        if (![validConsentTypes objectForKey:[key uppercaseString]]) {
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[NSString stringWithFormat:@"Invalid consent type: %@", key]];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            return;
        }

        if (![validConsentStatuses objectForKey:status]) {
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[NSString stringWithFormat:@"Invalid consent status for type %@: %@", key, status]];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            return;
        }
       
        FIRConsentType consentType = (FIRConsentType)[[validConsentTypes[[key uppercaseString]] integerValue]];
        FIRConsentStatus consentStatus = (FIRConsentStatus)[[validConsentStatuses[status] integerValue]];   

        [consentMap setObject:@(consentStatus) forKey:@(consentType)];
    }

    [FIRAnalytics setConsent:consentMap];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Consent settings updated"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


@end
