//
//  NewsTableViewCell.swift
//  MBurgerNews
//
//  Created by Lorenzo Oliveto on 02/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit

class NewsTableViewCell: UITableViewCell {
    
    @IBOutlet weak var newsImage: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!

    var imageDownloadTask: URLSessionTask?
    
    override func awakeFromNib() {
        super.awakeFromNib()

        titleLabel?.font = UIFont.boldSystemFont(ofSize: 17)
        dateLabel?.font = UIFont.systemFont(ofSize: 15)
        
        newsImage.contentMode = .scaleAspectFill
        newsImage.clipsToBounds = true
        newsImage.layer.cornerRadius = 5
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    func setup(withNews news: News) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .short
        dateFormatter.timeStyle = .short

        titleLabel.text = news.title
        
        dateLabel.text = dateFormatter.string(from: news.date)
                
        if let firstImage = news.images.first {
            imageDownloadTask = URLSession.shared.dataTask(with: firstImage.url, completionHandler: { data, _, _ in
                if let data = data {
                    DispatchQueue.main.async {
                        self.newsImage.image = UIImage(data: data)
                    }
                }
            })
            imageDownloadTask?.resume()
        }
        
        if news.inEvidence {
            accessoryView = evidenceView()
        } else {
            accessoryView = nil
        }
    }
    
    func evidenceView() -> UIView {
        let size: CGFloat = 10
        let evidenceView = UIView(frame: CGRect(x: 0, y: 0, width: size, height: size))
        evidenceView.backgroundColor = UIColor.red
        evidenceView.layer.cornerRadius = size / 2
        return evidenceView
    }
    
    override func prepareForReuse() {
        newsImage.image = nil
        if let imageDownloadTask = imageDownloadTask {
            imageDownloadTask.cancel()
        }
    }
}
