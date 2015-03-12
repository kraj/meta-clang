
# Add the necessary override
TOOLCHAINOVERRIDES = ":toolchain-${TOOLCHAIN}"
TOOLCHAINOVERRIDES[vardepsexclude] = "TOOLCHAIN"

OVERRIDES .= "${TOOLCHAINOVERRIDES}"
OVERRIDES[vardepsexclude] += "TOOLCHAINOVERRIDES"

require conf/clang.conf

