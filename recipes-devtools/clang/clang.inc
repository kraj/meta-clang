LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER="4"
MINOR_VER="0"
PATCH_VER="1"
PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_40"
SRCREV_llvm = "c8fccc53ed66d505898f8850bcc690c977a7c9a7"
SRCREV_clang = "3c8961bedc65c9a15cbe67a2ef385a0938f7cfef"
SRCREV_compiler-rt = "76ab2e5c9b2a2e3d638e217cc21622f9be54f633"
SRCREV_cxxabi = "c4e6c8a10dea6f569fa99d56945ec1eba4be40dd"
SRCREV_libcxx = "2a0436688e5f1cf233b5cef9d9d0a4dc6acf8a97"
SRCREV_libunwind = "e1a24d4951a60ea51a7bd2505d6cc16c0bcb2c61"
SRCREV_lld = "4439e42e1a3dcc6bf15fdb00114e4fc598b9d614"
SRCREV_lldb = "fcd2aac9f179b968a20cf0231c3386dcef8a6659"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
