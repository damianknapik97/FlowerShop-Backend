package com.dknapik.flowershop.dto.account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

public class PasswordChangeDto {
    private UUID id;
    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private String currentPassword;
    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private String newPassword;
    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private String newPasswordConfirmation;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    @Override
    public String toString() {
        return "PasswordChangeDto{" +
                "id=" + id +
                ", currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", newPasswordConfirmation='" + newPasswordConfirmation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordChangeDto that = (PasswordChangeDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (currentPassword != null ? !currentPassword.equals(that.currentPassword) : that.currentPassword != null)
            return false;
        if (newPassword != null ? !newPassword.equals(that.newPassword) : that.newPassword != null) return false;
        return newPasswordConfirmation != null ? newPasswordConfirmation.equals(that.newPasswordConfirmation) : that.newPasswordConfirmation == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (currentPassword != null ? currentPassword.hashCode() : 0);
        result = 31 * result + (newPassword != null ? newPassword.hashCode() : 0);
        result = 31 * result + (newPasswordConfirmation != null ? newPasswordConfirmation.hashCode() : 0);
        return result;
    }
}
