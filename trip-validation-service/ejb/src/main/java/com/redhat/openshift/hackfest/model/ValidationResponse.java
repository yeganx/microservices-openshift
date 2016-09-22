package com.redhat.openshift.hackfest.model;

/**
 * 
 * Validation response class used by the passport validation process
 * 
 * @author dsancho Sep 12, 2016
 *
 */
public class ValidationResponse {

	public enum ValidationCode {
		OK, REJECTED, PENDING, ERROR, NOT_FOUND
	}

	private ValidationCode validationCode;

	private String additionalInformation;

	private Long caseId;

	public ValidationResponse(ValidationCode validationCode) {
		this.validationCode = validationCode;
	}

	public ValidationCode getValidationCode() {
		return validationCode;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	@Override
	public String toString() {
		return "ValidationResponse [validationCode=" + validationCode + ", additionalInformation="
				+ (additionalInformation != null ? additionalInformation : "(none)") + ", caseId="
				+ (caseId != null ? caseId : "(none)") + "]";
	}

}
