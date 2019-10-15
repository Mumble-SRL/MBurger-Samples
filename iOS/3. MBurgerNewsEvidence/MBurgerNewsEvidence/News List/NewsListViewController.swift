//
//  ViewController.swift
//  MBurgerNews
//
//  Created by Lorenzo Oliveto on 02/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit
import MBurger

class NewsListViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    var categoryControl = UISegmentedControl()
    let refreshControl = UIRefreshControl()
    
    var inEvidence: MBEvidenceInformations?
    var news: [News]?
    
    var filterCategory: NewsCategory?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        checkNotifications()
        NotificationCenter.default.addObserver(self, selector: #selector(checkNotifications), name: NotificationsController.notificationArrivedNotification, object: nil)
        
        navigationController?.navigationBar.prefersLargeTitles = true
        navigationItem.title = "News"
        
        categoryControl.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: 31)
        categoryControl.backgroundColor = UIColor.white
        categoryControl.insertSegment(withTitle: "All", at: 0, animated: false)
        for (index, category) in NewsCategory.allCases.enumerated() {
            categoryControl.insertSegment(withTitle: category.rawValue.capitalized, at: index + 1, animated: false)
        }
        categoryControl.selectedSegmentIndex = 0
        categoryControl.addTarget(self, action: #selector(categoryChanged), for: .valueChanged)
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView()
        
        refreshControl.addTarget(self, action: #selector(loadData), for: .valueChanged)
        tableView.refreshControl = refreshControl
        loadData()
    }
    
    @objc func loadData() {
        loadInEvidence(completion: {
            self.loadNews()
            self.tableView.reloadData()
        }, failure: { error in
            self.showError(error: error)
        })
    }
    
    func loadInEvidence(completion: @escaping () -> Void,
                        failure: @escaping (Error) -> Void) {
        MBClient.getProjectWithSuccess({ project in
            self.inEvidence = project.evidenceInformations
            self.tableView.reloadData()
            completion()
        }, failure: failure)
    }
    
    func loadNews() {
        var parameters = [MBParameter]()
        if let filterCategory = filterCategory {
            parameters.append(MBFilterParameter(field: "elements.value",
                                                name: "category",
                                                value: filterCategory.rawValue))
        }
        MBClient.getSectionsWithBlockId(Configuration.newsBlockId,
                                        parameters: parameters,
                                        includeElements: true,
                                        success: { (sections, paginationInfo) in
                                            self.refreshControl.endRefreshing()
                                            self.news = sections.map({ News(section: $0) })
                                            //Reload only the first section
                                            self.tableView.reloadSections(IndexSet.init(integer: 1), with: .automatic)
        }, failure: { error in
            self.showError(error: error)
        })
    }

    func showError(error: Error) {
        let alertController = UIAlertController(title: "Error", message: error.localizedDescription, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil))
        present(alertController, animated: true, completion: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showDetail" {
            if let destination = segue.destination as? NewsDetailViewController, let indexPath = sender as? IndexPath {
                if let news = news?[indexPath.row] {
                    destination.news = news
                }
            }
            
            if let destination = segue.destination as? NewsDetailViewController, let sectionId = sender as? Int {
                destination.sectionId = sectionId
            }
        }
    }
    
    @objc func categoryChanged() {
        if categoryControl.selectedSegmentIndex == 0 {
            filterCategory = nil
        } else {
            filterCategory = NewsCategory.allCases[categoryControl.selectedSegmentIndex - 1]
        }
        loadNews()
    }
    
    @objc func checkNotifications() {
        if let payload = NotificationsController.shared.currentNotificationPayload {
            NotificationsController.shared.currentNotificationPayload = nil
            
            guard let sectionId = payload["section_id"] as? Int else {
                return
            }
            performSegue(withIdentifier: "showDetail", sender: sectionId)
        }
    }
    
    func showInEvidence() {
        if let evidence = inEvidence {
            performSegue(withIdentifier: "showDetail", sender: evidence.sectionId)
        }
    }
}

extension NewsListViewController: UITableViewDelegate, UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return inEvidence != nil ? 1 : 0
        } else if section == 1 {
            return news?.count ?? 0
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            if let cell = tableView.dequeueReusableCell(withIdentifier: "InEvidenceNewsTableViewCell") as? InEvidenceNewsTableViewCell, let evidence = inEvidence {
                cell.setup(withInEvidenceInformations: evidence)
                return cell
            }
        } else if indexPath.section == 1 {
            if let cell = tableView.dequeueReusableCell(withIdentifier: "NewsTableViewCell") as? NewsTableViewCell, let news = news?[indexPath.row] {
                cell.setup(withNews: news)
                return cell
            }
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if indexPath.section == 0 {
            showInEvidence()
        } else if indexPath.section == 1 {
            performSegue(withIdentifier: "showDetail", sender: indexPath)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0 {
            return 200
        } else if indexPath.section == 1 {
            return 100
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 1 {
            return categoryControl
        }
        return nil
    }

    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 1 {
            return categoryControl.frame.height
        }
        return 0
    }
}

