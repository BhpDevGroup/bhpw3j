package io.bhpw3j.model.types;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BhpAssetTest {

    @Test
    public void testField_HashId() {
        assertThat(BhpAsset.HASH_ID, is("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"));
    }

    @Test
    public void testField_Name() {
        assertThat(BhpAsset.NAME, is("BHP"));
    }

    @Test
    public void testField_Type() {
        assertThat(BhpAsset.TYPE, is(AssetType.GOVERNING_TOKEN));
        assertThat(BhpAsset.TYPE.jsonValue(), is("GoverningToken"));
    }

}
