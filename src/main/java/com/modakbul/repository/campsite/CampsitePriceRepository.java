package com.modakbul.repository.campsite;

import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.campsite.CampsitePrice;
import com.modakbul.entity.campsite.CampsitePriceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampsitePriceRepository extends JpaRepository<CampsitePrice, CampsitePriceId> {

}