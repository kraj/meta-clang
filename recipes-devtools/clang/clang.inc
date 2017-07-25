LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER="5"
MINOR_VER="0"
PATCH_VER="0"
PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_50"
SRCREV_llvm = "d175f9addcfa01156feb0711fabc70e7bb72b7cb"
SRCREV_clang = "4c3e691e191e1bef23ee65d4c8d8a640a51c6733"
SRCREV_compiler-rt = "e6bb43d8b68ab16a71b060fc32fcba18d20f8828"
SRCREV_cxxabi = "5df6b5da0deba63bbf9046bcaa385241c4d72847"
SRCREV_libcxx = "526c3a93942714925a19bdd6206b5698227575dc"
SRCREV_libunwind = "6c7b19f88102e05bcf6680073393b0b23e301cb9"
SRCREV_lld = "984850ba88f278092edb13cc86af88c8f75d85c4"
SRCREV_lldb = "d8edb413bc1766fd33290db3e1ddd8715f982dee"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
