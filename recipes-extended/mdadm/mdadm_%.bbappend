# Fix errors like
# | super-intel.c:1673:23: error: taking address of packed member 'size_high' of class or structure 'imsm_dev' may result in an unaligned pointer value [-Werror,-Waddress-of-packed-member]
# |                                  &dev->size_low, &dev->size_high);
# |                                                   ^~~~~~~~~~~~~~

CFLAGS:append:toolchain-clang = " -Wno-error=address-of-packed-member"
