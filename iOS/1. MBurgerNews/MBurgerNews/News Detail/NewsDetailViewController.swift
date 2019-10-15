//
//  NewsDetailViewController.swift
//  MBurgerNews
//
//  Created by Lorenzo Oliveto on 02/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit
import Down

class NewsDetailViewController: UIViewController {

    var news: News!
    
    @IBOutlet weak var scrollView: UIScrollView!

    @IBOutlet weak var labelTitle: UILabel!
    @IBOutlet weak var labelDate: UILabel!
    @IBOutlet weak var labelCategory: UILabel!
    @IBOutlet weak var labelContent: UILabel!

    @IBOutlet weak var imagesCollectionView: UICollectionView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationItem.title = news.title
        navigationItem.largeTitleDisplayMode = .never
        
        labelTitle.font = UIFont.boldSystemFont(ofSize: 17)
        labelTitle.numberOfLines = 0
        labelTitle.text = news.title
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .short
        dateFormatter.timeStyle = .short
        
        labelCategory.font = UIFont.systemFont(ofSize: 17)
        labelCategory.textColor = UIColor.gray
        labelCategory.numberOfLines = 0
        labelCategory.text = news.category?.rawValue.capitalized

        labelDate.font = UIFont.systemFont(ofSize: 17)
        labelDate.numberOfLines = 0
        labelDate.text = dateFormatter.string(from: news.date)

        labelContent.font = UIFont.systemFont(ofSize: 17)
        labelContent.numberOfLines = 0
        let down = Down(markdownString: news.content)
        labelContent.attributedText = try? down.toAttributedString()
        
        if let layout = imagesCollectionView.collectionViewLayout as? UICollectionViewFlowLayout {
            layout.scrollDirection = .horizontal
        }
        imagesCollectionView.delegate = self
        imagesCollectionView.dataSource = self
        imagesCollectionView.isPagingEnabled = true
        imagesCollectionView.showsHorizontalScrollIndicator = false
    }
}

extension NewsDetailViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return news.images.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "NewsImageCollectionViewCell", for: indexPath) as? NewsImageCollectionViewCell {
            let image = news.images[indexPath.item]
            cell.setup(imageUrl: image.url)
            return cell
        }
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return collectionView.bounds.size
    }
}
