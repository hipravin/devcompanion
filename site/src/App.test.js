import { render, screen } from '@testing-library/react';
import App from './App';

test('renders learn react link', () => {
  //TODO: update or drop this test

  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
