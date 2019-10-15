//
//  NewsImageCollectionViewCell.swift
//  MBurgerNews
//
//  Created by Lorenzo Oliveto on 03/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit

class NewsImageCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var imageView: UIImageView!
    var imageDownloadTask: URLSessionTask?

    func setup(imageUrl: URL) {
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageDownloadTask = URLSession.shared.dataTask(with: imageUrl, completionHandler: { data, _, _ in
            if let data = data {
                DispatchQueue.main.async {
                    self.imageView.image = UIImage(data: data)
                }
            }
        })
        imageDownloadTask?.resume()
    }
    
    override func prepareForReuse() {
        imageView.image = nil
        if let imageDownloadTask = imageDownloadTask {
            imageDownloadTask.cancel()
        }
    }
}
