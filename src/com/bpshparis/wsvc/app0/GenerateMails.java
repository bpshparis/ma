package com.bpshparis.wsvc.app0;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateMails {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		List<Mail> mails = new ArrayList<Mail>();
		
		Mail mail = new Mail();
		mail.setSubject("Trump Won’t Certify Nuclear Deal, but Won’t Unravel It");
		mail.setContent("President Trump’s decision not to certify that Iran was complying with all terms of the 2015 agreement "
				+ "reached by President Barack Obama came after a fierce debate inside the administration.");
		mail.setFace("trump.jpg");
		mails.add(mail);
		
		mail = new Mail();
		mail.setSubject("Wolf Puppies Are Adorable. Then Comes the Call of the Wild.");
				mail.setContent("When they are full-grown at around 100 pounds, their jaws will be strong enough to crack moose bones. "
				+ "But because these wolves have been around humans since they were blind, deaf and unable to stand, "
				+ "they will still allow people to be near them, to do veterinary exams, to scratch them behind the ears — if all goes well.");
		mail.setAttached("wolf.pdf");
		mails.add(mail);
		
		mail = new Mail();
		mail.setSubject("In California, Fires So Fast Hesitation Proved Lethal");
		mail.setContent("By THOMAS FULLER and RICHARD PÉREZ-PEÑA 12:00 AM ET. "
				+ "As stories emerged of how people died in the wind-driven fires that have ravaged Northern California, "
				+ "the element common to each tragedy was how quickly it happened.");
		mail.setUrl("https://www.nytimes.com/2017/10/13/us/california-wildfires-victims.html?"
				+ "ribbon-ad-idx=5&src=trending&module=Ribbon&version=context&region=Header&action=click&contentCollection=Trending&pgtype=article");
		mails.add(mail);
		
		mail = new Mail();
		mail.setSubject("At This Brooklyn Bar, It’s Just You, Your Date and the Barman");
		mail.setContent("Public drinking in New York can be a chore. Many of the city’s bars are about as intimate as a rush-hour subway. "
				+ "Others serve as the stalking grounds for happy-hour predators. When the suits and ties arrive, "
				+ "it can be hard to find a stool or to get your glass refilled.");
		mail.setAttached("bar.doc");
		mail.setPicture("bar.jpg");
		mails.add(mail);
		
		mail = new Mail();
		mail.setSubject("Trump Alienates America’s Allies and Hands Iran a Victory");
		mail.setContent("For months, President Trump has made no secret of his desire to tear up one of the Obama administration’s "
				+ "top national security achievements: the nuclear accord with Iran, formally known as the Joint Comprehensive Plan of Action, or J.C.P.O.A. "
				+ "Mr. Trump repeatedly bashed the agreement during last year’s campaign. "
				+ "Once in the White House, American law required the president to certify to Congress every 90 days that Iran was complying. "
				+ "Even Mr. Trump’s adversarial relationship with the truth could not dodge the fact that Iran is in compliance — as determined "
				+ "repeatedly by the International Atomic Energy Agency, the American intelligence community and our closest European allies. "
				+ "Mr. Trump has found it galling to certify — not once, but twice — that a deal he described as “the worst ever” and “an embarrassment” is working.");
		mail.setTip("iran.jpg");
		mails.add(mail);

		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		mapper.writeValue(sw, mails);
		sw.flush();
		System.out.println(sw.toString());

		BufferedWriter writer = null;
        try {
            File mailsFile = new File("/opt/wks/watson/app0/WebContent/res/mails/mails.json");
            writer = new BufferedWriter(new FileWriter(mailsFile));
            writer.write(sw.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
        		sw.close();		
                writer.close();
            } catch (Exception e) {
            }
        }
		
	}

}
