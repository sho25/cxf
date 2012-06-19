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
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
comment|//CHECKSTYLE:OFF
specifier|public
class|class
name|ParamJAXBElement
extends|extends
name|JAXBElement
argument_list|<
name|ParamType
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4994571526736505284L
decl_stmt|;
specifier|protected
specifier|final
specifier|static
name|QName
name|NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://jaxbelement/10"
argument_list|,
literal|"param"
argument_list|)
decl_stmt|;
specifier|public
name|ParamJAXBElement
parameter_list|(
name|ParamType
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|NAME
argument_list|,
operator|(
operator|(
name|Class
operator|)
name|ParamType
operator|.
name|class
operator|)
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ParamJAXBElement
parameter_list|()
block|{
name|super
argument_list|(
name|NAME
argument_list|,
operator|(
operator|(
name|Class
operator|)
name|ParamType
operator|.
name|class
operator|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

