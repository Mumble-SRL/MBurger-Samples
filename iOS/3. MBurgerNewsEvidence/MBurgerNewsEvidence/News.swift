//
//  News.swift
//  MBurgerNews
//
//  Created by Lorenzo Oliveto on 02/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit
import MBurger

enum NewsCategory: String, CaseIterable {
    case sport
    case tech
    case society
}

class News: NSObject {
    var newsId: Int!
    @objc var title: String!
    @objc var content: String!
    @objc var images: [MBImage]!
    @objc var date: Date!
    var category: NewsCategory!
    var inEvidence: Bool!

    convenience init(section: MBSection) {
        self.init()
        newsId = section.sectionId
        section.mapElements(to: self, withMapping: ["title": "title",
                                                    "content": "content",
                                                    "images": "images"])
        
        date = section.availableAt
        inEvidence = section.inEvidence

        if let categoryDropdown = section.elements?["category"] as? MBDropdownElement {
            category = NewsCategory(rawValue: categoryDropdown.selectedOption ?? "tech") ?? .tech
        } else { /// Defaults to tech
            category = .tech
        }
    }
}
