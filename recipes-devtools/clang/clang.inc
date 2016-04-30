LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

PV ?= "3.8.0"
BRANCH ?= "release_38"
SRCREV_llvm ?= "06cf4b238fa45638ed37ec7725d38930fea37ff9"
SRCREV_clang ?= "9fd77bd68130d9b2fbc56a3138b6f981d560480a"
SRCREV_compiler-rt ?= "31769223eb782e7b03655810d00dfdec66467d51"
SRCREV_libcxxabi ?= "8d4e51dd9c743b66c8dd552f0632174e04af4052"
SRCREV_libcxx ?= "fda3549c01ea0a1d2ee00fe0033c35dc36796fc8"
SRCREV_libunwind ?= "25647b29dd78196ed3c883392ebb339265aeb355"
