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
SRCREV_llvm = "a971137ca9907d1f9c630915f11f8f3d42a3cb84"
SRCREV_clang = "8df2b6dec1451e65ab500939da3e9bf394236a42"
SRCREV_compiler-rt = "e6bb43d8b68ab16a71b060fc32fcba18d20f8828"
SRCREV_cxxabi = "5df6b5da0deba63bbf9046bcaa385241c4d72847"
SRCREV_libcxx = "ef34b82889be4aa6438da96868f3c5d9fd38edcc"
SRCREV_libunwind = "08a9d8430b8096d8963e9bc9c2a05f32816dd8a0"
SRCREV_lld = "a9f9e258f5e7f636bd35a53d48ac4bcaefa0a6dc"
SRCREV_lldb = "d8edb413bc1766fd33290db3e1ddd8715f982dee"
SRCREV_openmp = "1333cd34fa88de8461c0dbd2fbd439321073a334"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
