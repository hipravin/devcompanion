import {queryToTerms} from "./lib/highlight";

test('parse query', () => {
  const text = "java   BigDecimal \t   compare  "

  const terms = queryToTerms(text);

  console.log(terms);

  expect(terms.length).toBe(3);
});


