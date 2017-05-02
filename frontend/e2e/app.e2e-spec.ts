import { GdisClientPage } from './app.po';

describe('gdis-client App', () => {
  let page: GdisClientPage;

  beforeEach(() => {
    page = new GdisClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
