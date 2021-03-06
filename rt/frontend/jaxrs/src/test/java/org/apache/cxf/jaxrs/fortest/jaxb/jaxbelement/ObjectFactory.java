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
name|jaxrs
operator|.
name|fortest
operator|.
name|jaxb
operator|.
name|jaxbelement
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|annotation
operator|.
name|XmlElementDecl
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
name|annotation
operator|.
name|XmlRegistry
import|;
end_import

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

begin_class
annotation|@
name|XmlRegistry
comment|//CHECKSTYLE:OFF
specifier|public
class|class
name|ObjectFactory
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|_ParamJAXBElement_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://jaxbelement/10"
argument_list|,
literal|"comment"
argument_list|)
decl_stmt|;
specifier|public
name|ObjectFactory
parameter_list|()
block|{     }
specifier|public
name|ParamType
name|createParamTypeTO
parameter_list|()
block|{
return|return
operator|new
name|ParamType
argument_list|()
return|;
block|}
annotation|@
name|XmlElementDecl
argument_list|(
name|namespace
operator|=
literal|"http://jaxbelement/10"
argument_list|,
name|name
operator|=
literal|"param"
argument_list|)
specifier|public
name|ParamJAXBElement
name|createParamJAXBElement
parameter_list|(
name|ParamType
name|value
parameter_list|)
block|{
return|return
operator|new
name|ParamJAXBElement
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|XmlElementDecl
argument_list|(
name|namespace
operator|=
literal|"http://jaxbelement/10"
argument_list|,
name|name
operator|=
literal|"comment"
argument_list|)
specifier|public
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|createRevocationRemark
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
name|_ParamJAXBElement_QNAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

