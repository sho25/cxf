begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|getAttributedURI
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|getMAPProperty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|getRelatesTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|hasEmptyAction
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|isAnonymousAddress
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|isFault
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|isGenericAddress
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|isNoneAddress
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|isOutbound
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|isRequestor
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|retrieveMAPs
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
operator|.
name|storeMAPs
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
operator|.
name|ADDRESSING_PROPERTIES_INBOUND
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
operator|.
name|ADDRESSING_PROPERTIES_OUTBOUND
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|notNullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsEqual
operator|.
name|equalTo
import|;
end_import

begin_class
specifier|public
class|class
name|ContextUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testIsOutbound
parameter_list|()
block|{
name|assertThat
argument_list|(
name|isOutbound
argument_list|(
literal|null
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|isOutbound
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isOutbound
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isOutbound
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutFaultMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isOutbound
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsFault
parameter_list|()
block|{
name|assertThat
argument_list|(
name|isFault
argument_list|(
literal|null
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|isFault
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isFault
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInFaultMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isFault
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInFaultMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutFaultMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isFault
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsRequestor
parameter_list|()
block|{
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMAPProperty
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getMAPProperty
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
argument_list|,
name|is
argument_list|(
name|CLIENT_ADDRESSING_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMAPProperty
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|,
name|is
argument_list|(
name|CLIENT_ADDRESSING_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMAPProperty
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
argument_list|,
name|is
argument_list|(
name|ADDRESSING_PROPERTIES_OUTBOUND
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMAPProperty
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|,
name|is
argument_list|(
name|ADDRESSING_PROPERTIES_INBOUND
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreMAPs
parameter_list|()
block|{
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|ADDRESSING_PROPERTIES_OUTBOUND
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|maps
argument_list|)
argument_list|)
expr_stmt|;
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|ADDRESSING_PROPERTIES_INBOUND
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|maps
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRetrieveMAPs
parameter_list|()
block|{
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|maps
argument_list|)
argument_list|)
expr_stmt|;
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|maps
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAttributedURI
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getAttributedURI
argument_list|(
literal|null
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|is
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|value
init|=
literal|"test"
decl_stmt|;
name|assertThat
argument_list|(
name|getAttributedURI
argument_list|(
name|value
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|is
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRelatesTo
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getRelatesTo
argument_list|(
literal|null
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|is
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|value
init|=
literal|"test"
decl_stmt|;
name|assertThat
argument_list|(
name|getRelatesTo
argument_list|(
name|value
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|is
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsGenericAddress
parameter_list|()
block|{
name|assertThat
argument_list|(
name|isGenericAddress
argument_list|(
literal|null
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|ref
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isGenericAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isGenericAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|setValue
argument_list|(
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|+
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isGenericAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|setValue
argument_list|(
name|Names
operator|.
name|WSA_NONE_ADDRESS
operator|+
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isGenericAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsAnonymousAddress
parameter_list|()
block|{
name|assertThat
argument_list|(
name|isAnonymousAddress
argument_list|(
literal|null
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|ref
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isAnonymousAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isAnonymousAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|setValue
argument_list|(
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|+
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isAnonymousAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsNoneAddress
parameter_list|()
block|{
name|assertThat
argument_list|(
name|isNoneAddress
argument_list|(
literal|null
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|ref
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|isNoneAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isNoneAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|setValue
argument_list|(
name|Names
operator|.
name|WSA_NONE_ADDRESS
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isNoneAddress
argument_list|(
name|ref
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHasEmptyAddress
parameter_list|()
block|{
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|hasEmptyAction
argument_list|(
name|maps
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|maps
operator|.
name|setAction
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|setValue
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|hasEmptyAction
argument_list|(
name|maps
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|maps
operator|.
name|getAction
argument_list|()
argument_list|,
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|maps
operator|.
name|setAction
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|hasEmptyAction
argument_list|(
name|maps
argument_list|)
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|maps
operator|.
name|getAction
argument_list|()
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

