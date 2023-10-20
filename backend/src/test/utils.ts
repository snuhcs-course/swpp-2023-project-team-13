export function validateDtoKeys(dto: any, keys: string[]) {
  const dtoKeys = Object.keys(dto);
  for (const key of keys) {
    expect(dtoKeys).toContain(key);
  }
}
