//
//  Author.swift
//  MBurgerNewsRelation
//
//  Created by Lorenzo Oliveto on 22/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit
import MBurger

class Author: NSObject {
    var authorId: Int!
    @objc var name: String!
    @objc var about: String!
    @objc var image: MBImage!

    convenience init(section: MBSection) {
        self.init()
        authorId = section.sectionId
        section.mapElements(to: self, withMapping: ["name": "name",
                                                    "about-me": "about",
                                                    "photo.firstImage": "image"])
    }

}
