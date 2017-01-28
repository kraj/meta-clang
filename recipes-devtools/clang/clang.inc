LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "release_40"
SRCREV_llvm = "50c188766385964a57d93a9db1bff024fce8dd82"
SRCREV_clang = "7bf54fb33971d145af18ce1ed4b7344df1d8a26f"
SRCREV_compiler-rt = "3e677c3078269d72252faa6ec1d4b944995cb76d"
SRCREV_cxxabi = "c980f401c96324d8d5210fbbf9ba64154a740e7b"
SRCREV_libcxx = "b53ec893f455b6b258d17f6ac99473a12cf25af9"
SRCREV_libunwind = "9f88e323c3d0926f52ab3d4a5379eefd92ff28c2"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
