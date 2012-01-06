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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
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
name|BusFactory
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
name|databinding
operator|.
name|WrapperHelper
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|jaxws
operator|.
name|service
operator|.
name|AddNumbersImpl
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsImplementorInfo
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|model
operator|.
name|InterfaceInfo
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
specifier|public
class|class
name|WrapperClassGeneratorTest
extends|extends
name|Assert
block|{
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testForXmlList
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsImplementorInfo
name|implInfo
init|=
operator|new
name|JaxWsImplementorInfo
argument_list|(
name|AddNumbersImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|JaxWsServiceFactoryBean
name|jaxwsFac
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|(
name|implInfo
argument_list|)
decl_stmt|;
name|jaxwsFac
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|jaxwsFac
operator|.
name|create
argument_list|()
decl_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|InterfaceInfo
name|interfaceInfo
init|=
name|serviceInfo
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|OperationInfo
name|inf
init|=
name|interfaceInfo
operator|.
name|getOperations
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|requestClass
init|=
name|inf
operator|.
name|getInput
argument_list|()
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
init|=
name|inf
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
comment|// Create request wrapper Object
name|List
argument_list|<
name|String
argument_list|>
name|partNames
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"arg0"
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|elTypeNames
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"list"
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|partClasses
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|List
operator|.
name|class
block|}
block|)
function|;
name|String
name|className
init|=
name|requestClass
operator|.
name|getName
argument_list|()
decl_stmt|;
name|className
operator|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|WrapperHelper
name|wh
init|=
operator|new
name|JAXBDataBinding
argument_list|()
operator|.
name|createWrapperHelper
argument_list|(
name|requestClass
argument_list|,
literal|null
argument_list|,
name|partNames
argument_list|,
name|elTypeNames
argument_list|,
name|partClasses
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|paraList
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|valueList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|valueList
operator|.
name|add
argument_list|(
literal|"str1"
argument_list|)
expr_stmt|;
name|valueList
operator|.
name|add
argument_list|(
literal|"str2"
argument_list|)
expr_stmt|;
name|valueList
operator|.
name|add
argument_list|(
literal|"str3"
argument_list|)
expr_stmt|;
name|paraList
operator|.
name|add
parameter_list|(
name|valueList
parameter_list|)
constructor_decl|;
name|Object
name|requestObj
init|=
name|wh
operator|.
name|createWrapperObject
argument_list|(
name|paraList
argument_list|)
decl_stmt|;
comment|// Create response wrapper Object
name|partNames
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"return"
block|}
argument_list|)
expr_stmt|;
name|elTypeNames
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"list"
block|}
argument_list|)
expr_stmt|;
name|partClasses
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|List
operator|.
name|class
block|}
block|)
class|;
end_class

begin_expr_stmt
name|className
operator|=
name|responseClass
operator|.
name|getName
argument_list|()
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|className
operator|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|wh
operator|=
operator|new
name|JAXBDataBinding
argument_list|()
operator|.
name|createWrapperHelper
argument_list|(
name|responseClass
argument_list|,
literal|null
argument_list|,
name|partNames
argument_list|,
name|elTypeNames
argument_list|,
name|partClasses
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|List
argument_list|<
name|Object
argument_list|>
name|resPara
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|List
argument_list|<
name|Integer
argument_list|>
name|intValueList
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|intValueList
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|intValueList
operator|.
name|add
argument_list|(
literal|2
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|intValueList
operator|.
name|add
argument_list|(
literal|3
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|resPara
operator|.
name|add
argument_list|(
name|intValueList
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|Object
name|responseObj
init|=
name|wh
operator|.
name|createWrapperObject
argument_list|(
name|resPara
argument_list|)
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|JAXBContext
name|jaxbContext
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|requestClass
argument_list|,
name|responseClass
argument_list|)
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|Marshaller
name|marshaller
init|=
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
end_decl_stmt

begin_comment
comment|//check marshall wrapper
end_comment

begin_expr_stmt
name|marshaller
operator|.
name|marshal
argument_list|(
name|requestObj
argument_list|,
name|bout
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|String
name|expected
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
operator|+
literal|"<ns2:addNumbers xmlns:ns2=\"http://service.jaxws.cxf.apache.org/\">"
operator|+
literal|"<arg0>str1 str2 str3</arg0></ns2:addNumbers>"
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|assertEquals
argument_list|(
literal|"The generated request wrapper class does not contain the correct annotations"
argument_list|,
name|expected
argument_list|,
name|bout
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|bout
operator|.
name|reset
argument_list|()
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|marshaller
operator|.
name|marshal
argument_list|(
name|responseObj
argument_list|,
name|bout
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|expected
operator|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
operator|+
literal|"<ns2:addNumbersResponse xmlns:ns2=\"http://service.jaxws.cxf.apache.org/\">"
operator|+
literal|"<return>1</return><return>2</return><return>3</return></ns2:addNumbersResponse>"
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|assertEquals
argument_list|(
literal|"The generated response wrapper class is not correct"
argument_list|,
name|expected
argument_list|,
name|bout
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

unit|}    }
end_unit

