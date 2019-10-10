//
//  AppDelegate.swift
//  MBurgerNewsPush
//
//  Created by Alessandro Viviani on 10/10/2019.
//  Copyright © 2019 Mumble S.r.l (https://mumbleideas.it). All rights reserved.
//

import UIKit
import MPushSwift
import UserNotifications
import MBurger

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        MPush.token = Configuration.mpushToken
        MBManager.shared().apiToken = Configuration.mburgerToken
        
        let userNotificationCenter = UNUserNotificationCenter.current()
        userNotificationCenter.delegate = self
        
        registerForPushNotifications()
        return true
    }

    func registerForPushNotifications() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { (granted, _) in
            guard granted else { return }
            self.getNotificationSettings()
        }
    }
    
    func getNotificationSettings() {
        UNUserNotificationCenter.current().getNotificationSettings { (settings) in
            guard settings.authorizationStatus == .authorized else { return }
            DispatchQueue.main.async {
                NotificationsController.shared.pushNotificationsEnabled = true
                UIApplication.shared.registerForRemoteNotifications()
            }
        }
    }
    // MARK: UISceneSession Lifecycle
    
    @available(iOS 13.0, *)
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    @available(iOS 13.0, *)
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }

    // MARK: Notifications
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        MPush.registerDevice(deviceToken: deviceToken, success: {
            MPush.register(toTopic: Configuration.mpushTopic)
        })
    }
}

// MARK: - Notifications Handling
extension AppDelegate: UNUserNotificationCenterDelegate {
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        handleNotificationDictionary(userInfo: userInfo)
        completionHandler(UIBackgroundFetchResult.noData)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler(UNNotificationPresentationOptions.alert)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        handleNotificationDictionary(userInfo: response.notification.request.content.userInfo)
        completionHandler()
    }
    
    func handleNotificationDictionary(userInfo: [AnyHashable: Any]) {
        NotificationsController.shared.currentNotificationPayload = userInfo as? [String: Any]
        NotificationCenter.default.post(name: NotificationsController.notificationArrivedNotification, object: nil)
        UIApplication.shared.applicationIconBadgeNumber = 0
    }
}
