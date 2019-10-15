//
//  NotificationsController.swift
//  MBurgerNewsPush
//
//  Created by Alessandro Viviani on 10/10/2019.
//  Copyright © 2019 Mumble S.r.l (https://mumbleideas.it). All rights reserved.
//

import UIKit

class NotificationsController {
    static let shared = NotificationsController()
    
    static let notificationArrivedNotification: NSNotification.Name = NSNotification.Name(rawValue: "com.mumble.MBurgerNewsPush.notificationArrived")

    var currentNotificationPayload: [String: Any]?
    
    var pushNotificationsEnabled: Bool {
        get {
            return UserDefaults.standard.value(forKey: "com.mumble.MBurgerNewsPush.enablePush") as? Bool ?? false
        }
        set {
            UserDefaults.standard.setValue(newValue, forKey: "com.mumble.MBurgerNewsPush.enablePush")
        }
    }
}
