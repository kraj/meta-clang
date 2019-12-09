LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm"
LLVM_GIT_PROTOCOL ?= "https"

MAJOR_VER = "8"
MINOR_VER = "0"
PATCH_VER = "1"

SRCREV ?= "89de0d8dfbb9a6ff1f8b141ed70b563ecc094878"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release/${MAJOR_VER}.x"

LLVMMD5SUM = "c6b766a4e85dd28301eeed54a6684648"
CLANGMD5SUM = "9a0fe3a7846ba0ffc822a70d6f7f6903"
LLDMD5SUM = "f4941ace8ddb3d6cf177fff94966319a"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"

def get_libdir_suffix(d, arch_var):
    import re
    multilibs = (d.getVar("MULTILIB_VARIANTS") or "").split()
    if multilibs:
        a = d.getVar(arch_var, True)
        if   re.match('(i.86|athlon)$', a):          return '32'
        elif re.match('x86.64$', a):                 return '64'
        elif re.match('(arm|armbe)$', a):            return '32'
        elif re.match('(aarch64|aarch64_be)$', a):   return '64'
        elif re.match('mips(isa|)32(r6|)(el|)$', a): return '32'
        elif re.match('mips(isa|)64(r6|)(el|)$', a): return '64'
        elif re.match('p(pc|owerpc)', a):            return '32'
        elif re.match('p(pc|owerpc)64', a):          return '64'
    else:
        return ''

LLVM_LIBDIR_SUFFIX="${@get_libdir_suffix(d, 'TARGET_ARCH')}"

require common.inc
