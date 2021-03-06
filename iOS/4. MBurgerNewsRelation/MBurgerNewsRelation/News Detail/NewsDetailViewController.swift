//
//  NewsDetailViewController.swift
//  MBurgerNews
//
//  Created by Lorenzo Oliveto on 02/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit
import Down
import MBurger

class NewsDetailViewController: UIViewController {
    var news: News?
    var sectionId: Int?
    
    @IBOutlet weak var scrollView: UIScrollView!

    @IBOutlet weak var labelTitle: UILabel!
    @IBOutlet weak var labelDate: UILabel!
    @IBOutlet weak var labelCategory: UILabel!
    @IBOutlet weak var labelContent: UILabel!
    
    @IBOutlet weak var imagesCollectionView: UICollectionView!

    @IBOutlet weak var authorSeparatorView: UIView!
    @IBOutlet weak var authorView: AuthorView!

    @IBOutlet var scrollViewBottomConstraintContent: NSLayoutConstraint!
    @IBOutlet var scrollViewBottomConstraintAuthor: NSLayoutConstraint!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let layout = imagesCollectionView.collectionViewLayout as? UICollectionViewFlowLayout {
            layout.scrollDirection = .horizontal
        }
        imagesCollectionView.delegate = self
        imagesCollectionView.dataSource = self
        imagesCollectionView.isPagingEnabled = true
        imagesCollectionView.showsHorizontalScrollIndicator = false
        
        authorSeparatorView.backgroundColor = UIColor(white: 0.9, alpha: 1)
        
        showAuthorView(show: false)
        if let news = self.news {
            setupView(news: news)
        } else if let sectionId = self.sectionId {
            getSection(sectionId)
        }
    }
    
    private func getSection(_ id: Int) {
        self.scrollView.isHidden = true
        MBClient.getSectionWithId(id, includeElements: true, success: { section in
            let news = News(section: section)
            self.news = news
            self.setupView(news: news)
            self.scrollView.isHidden = false
        }, failure: { error in
            print(error)
            self.scrollView.isHidden = false
        })
    }
    
    private func setupView(news: News) {
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
        
        imagesCollectionView.reloadData()
        
        if news.author != nil {
            self.scrollView.isHidden = true
            self.loadAuthor(withNews: news)
        }
    }
    
    // MARK: - Author
    
    private func loadAuthor(withNews news: News) {
        guard let author = news.author else {
            return
        }
        MBClient.getSectionWithId(author.sectionId, includeElements: true, success: { section in
            let author = Author(section: section)
            self.authorView.setup(withAuthor: author)
            self.showAuthorView(show: true)
            self.scrollView.isHidden = false
        }, failure: { error in
            print(error)
            self.scrollView.isHidden = false
        })
    }
    
    // Shows or hide the author view
    private func showAuthorView(show: Bool) {
        authorSeparatorView.isHidden = !show
        authorView.isHidden = !show
        if show {
            scrollViewBottomConstraintContent.isActive = false
            scrollViewBottomConstraintAuthor.isActive = true
        } else {
            scrollViewBottomConstraintAuthor.isActive = false
            scrollViewBottomConstraintContent.isActive = true
        }
        view.layoutIfNeeded()
    }
}

extension NewsDetailViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return news?.images.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "NewsImageCollectionViewCell", for: indexPath) as? NewsImageCollectionViewCell, let news = self.news {
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
