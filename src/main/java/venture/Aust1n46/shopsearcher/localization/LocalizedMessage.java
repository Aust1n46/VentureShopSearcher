package venture.Aust1n46.shopsearcher.localization;

import venture.Aust1n46.shopsearcher.utilities.FormatUtils;

/**
 * Messages configurable in Messages.yml file.
 */
public enum LocalizedMessage {
    COMMAND_INVALID_ARGUMENTS("CommandInvalidArguments"),
    COMMAND_MUST_BE_RUN_BY_PLAYER("CommandMustBeRunByPlayer"),
    COMMAND_NO_PERMISSION("CommandNoPermission"),
    PLAYER_OFFLINE("PlayerOffline");

	private final String message;

	LocalizedMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return FormatUtils.FormatStringAll(Localization.getLocalization().getString(this.message));
	}
}
