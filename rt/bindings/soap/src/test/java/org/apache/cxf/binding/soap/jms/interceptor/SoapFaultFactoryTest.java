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
name|binding
operator|.
name|soap
operator|.
name|jms
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|binding
operator|.
name|soap
operator|.
name|Soap12
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
name|binding
operator|.
name|soap
operator|.
name|SoapBinding
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|SoapFaultFactoryTest
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|JMSFault
name|jmsFault
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
block|}
name|JMSFault
name|setupJMSFault
parameter_list|(
name|boolean
name|isSender
parameter_list|,
name|QName
name|code
parameter_list|,
name|Object
name|detail
parameter_list|,
name|boolean
name|isSoap12
parameter_list|)
block|{
name|jmsFault
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|JMSFault
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|jmsFault
operator|.
name|getReason
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"reason"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSoap12
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|jmsFault
operator|.
name|isSender
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|isSender
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|jmsFault
operator|.
name|getSubCode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|code
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|detail
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|jmsFault
operator|.
name|getDetail
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|JMSFaultType
name|sft
init|=
operator|new
name|JMSFaultType
argument_list|()
decl_stmt|;
name|sft
operator|.
name|setFaultCode
argument_list|(
name|SoapJMSConstants
operator|.
name|getContentTypeMismatchQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|jmsFault
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createSoap11Fault
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|setupJMSFault
argument_list|(
literal|true
argument_list|,
name|SoapJMSConstants
operator|.
name|getContentTypeMismatchQName
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|factory
operator|.
name|createFault
argument_list|(
name|jmsFault
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reason"
argument_list|,
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SoapJMSConstants
operator|.
name|getContentTypeMismatchQName
argument_list|()
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|fault
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|jmsFault
argument_list|,
name|fault
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createSoap12Fault
parameter_list|()
block|{
name|SoapBinding
name|sb
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SoapBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sb
operator|.
name|getSoapVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|setupJMSFault
argument_list|(
literal|true
argument_list|,
name|SoapJMSConstants
operator|.
name|getMismatchedSoapActionQName
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SoapFaultFactory
name|factory
init|=
operator|new
name|SoapFaultFactory
argument_list|(
name|sb
argument_list|)
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|(
name|SoapFault
operator|)
name|factory
operator|.
name|createFault
argument_list|(
name|jmsFault
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reason"
argument_list|,
name|fault
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|fault
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SoapJMSConstants
operator|.
name|getMismatchedSoapActionQName
argument_list|()
argument_list|,
name|fault
operator|.
name|getSubCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|fault
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|fault
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

