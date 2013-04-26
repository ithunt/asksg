package edu.rit.asksg.service;

import com.google.common.base.Optional;
import edu.rit.asksg.domain.Identity;
import edu.rit.asksg.domain.Person;
import edu.rit.asksg.specification.EqualSpecification;
import edu.rit.asksg.specification.Specification;
import edu.rit.asksg.specification.TrueSpecification;

public class IdentityServiceImpl implements IdentityService {

	/**
	 * Checks if a string may match an identity format
	 *
	 * @param candidate
	 * @return true if
	 */
	public boolean isIdentity(String candidate) {
		if (candidate.contains("@"))
			return true;
		// check regex on phone #
		if (candidate.replaceAll("[^\\d.]", "").length() > 4)
			return true;
		return false;
	}

	@Override
	public Identity findOrCreate(String name) {
		Optional<Identity> identityOptional = searchIdentity(name);
		if (identityOptional.isPresent()) {
			return identityOptional.get();
		}
		//create and save new person - if identity was an asksguser it would have been found
		Person person = new Person();
		if (name.contains("@")) {
			person.setEmail(name);
		}
		//check if after removing all nonnumeric length implies phone number (short codes included)
		else if (name.replaceAll("[^\\d.]", "").length() > 4) {
			person.setPhoneNumber(name);
		} else {
			person.setName(name);
		}
		return identityRepository.save(person);
	}

	@Override
	public Optional<Identity> searchIdentity(String query) {
		EqualSpecification<Identity> checkName = new EqualSpecification<Identity>("name", query);
		EqualSpecification<Identity> checkPhone = new EqualSpecification<Identity>("phoneNumber", query);
		EqualSpecification<Identity> checkEmail = new EqualSpecification<Identity>("email", query);

		checkEmail.or(checkName).or(checkPhone);

		return Optional.fromNullable(identityRepository.findOne(checkEmail));
	}
}
