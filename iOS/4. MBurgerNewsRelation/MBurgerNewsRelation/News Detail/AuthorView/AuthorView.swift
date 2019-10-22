//
//  AuthorView.swift
//  MBurgerNewsRelation
//
//  Created by Lorenzo Oliveto on 22/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit

class AuthorView: UIView {
    @IBOutlet weak var profileImageView: UIImageView!
    @IBOutlet weak var labelName: UILabel!
    @IBOutlet weak var labelAbout: UILabel!

    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        commonInit()
    }
    
    private func commonInit() {
        if let viewsToAdd = Bundle.main.loadNibNamed("AuthorView", owner: self, options: nil),
            let contentView = viewsToAdd.first as? UIView {
            contentView.translatesAutoresizingMaskIntoConstraints = false
            addSubview(contentView)
            NSLayoutConstraint.activate([
                contentView.leadingAnchor.constraint(equalTo: leadingAnchor),
                contentView.trailingAnchor.constraint(equalTo: trailingAnchor),
                contentView.topAnchor.constraint(equalTo: topAnchor),
                contentView.bottomAnchor.constraint(equalTo: bottomAnchor),
            ])
            
            labelName.font = UIFont.boldSystemFont(ofSize: 17)
            labelName.numberOfLines = 1
            labelName.lineBreakMode = .byTruncatingTail

            labelAbout.font = UIFont.systemFont(ofSize: 12)
            labelAbout.numberOfLines = 2
            labelAbout.lineBreakMode = .byTruncatingTail
        }
    }
    
    func setup(withAuthor author: Author) {
        profileImageView.contentMode = .scaleAspectFill
        profileImageView.clipsToBounds = true
        profileImageView.layer.cornerRadius = 60 / 2
        
        let imageDownloadTask = URLSession.shared.dataTask(with: author.image.url, completionHandler: { data, _, _ in
            if let data = data {
                DispatchQueue.main.async {
                    self.profileImageView.image = UIImage(data: data)
                }
            }
        })
        imageDownloadTask.resume()
        
        labelName.text = author.name
        labelAbout.text = author.about
    }
}
