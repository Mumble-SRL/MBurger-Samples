//
//  InEvidenceNewsTableViewCell.swift
//  MBurgerNewsEvidence
//
//  Created by Lorenzo Oliveto on 15/10/2019.
//  Copyright © 2019 Lorenzo Oliveto. All rights reserved.
//

import UIKit
import MBurger

class InEvidenceNewsTableViewCell: UITableViewCell {

    @IBOutlet weak var evidenceImageView: UIImageView!
    @IBOutlet weak var evidenceBlackOverlay: UIView!
    @IBOutlet weak var evidenceTitleLabel: UILabel!

    var imageDownloadTask: URLSessionTask?

    override func awakeFromNib() {
        super.awakeFromNib()

        evidenceTitleLabel.font = UIFont.boldSystemFont(ofSize: 30)
        evidenceTitleLabel.numberOfLines = 2
        evidenceTitleLabel.textColor = UIColor.white
        evidenceTitleLabel.textAlignment = .center
        
        evidenceBlackOverlay.backgroundColor = UIColor.black.withAlphaComponent(0.3)
        
        evidenceImageView.contentMode = .scaleAspectFill
        evidenceImageView.clipsToBounds = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    func setup(withInEvidenceInformations inEvidenceInformations: MBEvidenceInformations) {
        evidenceTitleLabel.text = inEvidenceInformations.title
        
        let imageUrl = inEvidenceInformations.imageUrl
        imageDownloadTask = URLSession.shared.dataTask(with: imageUrl, completionHandler: { data, _, _ in
            if let data = data {
                DispatchQueue.main.async {
                    self.evidenceImageView.image = UIImage(data: data)
                }
            }
        })
        imageDownloadTask?.resume()
    }
    
    override func prepareForReuse() {
        evidenceImageView.image = nil
        if let imageDownloadTask = imageDownloadTask {
            imageDownloadTask.cancel()
        }
    }

}
