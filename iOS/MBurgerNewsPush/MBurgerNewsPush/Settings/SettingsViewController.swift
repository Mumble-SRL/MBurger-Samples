//
//  SettingsViewController.swift
//  MBurgerNewsPush
//
//  Created by Alessandro Viviani on 10/10/2019.
//  Copyright © 2019 Mumble S.r.l (https://mumbleideas.it). All rights reserved.
//

import UIKit
import MPushSwift

class SettingsViewController: UIViewController {
    @IBOutlet var tableView: UITableView!
    
    private let rowTitle = "Push On/Off"
    
    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.largeTitleDisplayMode = .never
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.rowHeight = 60
        tableView.separatorStyle = .none
    }
    
    @objc func switchValueChanged(_ sender: UISwitch) {
        NotificationsController.shared.pushNotificationsEnabled = sender.isOn
        
        sender.isOn ? subscribe() : unsubscribe()
    }
    
    fileprivate func unsubscribe() {
        MPush.unregister(fromTopic: Configuration.mpushTopic)
    }
    
    fileprivate func subscribe() {
        MPush.register(toTopic: Configuration.mpushTopic)
    }
}

extension SettingsViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: "Cell")

        if cell == nil {
            cell = UITableViewCell(style: .default, reuseIdentifier: "Cell")
        }
        
        let switchView = UISwitch(frame: .zero)
        switchView.setOn(NotificationsController.shared.pushNotificationsEnabled, animated: true)
        switchView.tag = indexPath.row
        switchView.addTarget(self, action: #selector(switchValueChanged(_:)), for: .valueChanged)
        cell?.accessoryView = switchView
        cell?.textLabel?.text = rowTitle
        return cell ?? UITableViewCell()
    }
}
