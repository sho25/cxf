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
name|wstx_msv_validation
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|Bus
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
name|io
operator|.
name|StaxValidationManager
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|WoodstoxValidationImpl
implements|implements
name|StaxValidationManager
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Stax2ValidationUtils
name|utils
decl_stmt|;
annotation|@
name|Resource
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|register
parameter_list|()
block|{
try|try
block|{
name|utils
operator|=
operator|new
name|Stax2ValidationUtils
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* If the dependencies are missing ... */
return|return;
block|}
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|StaxValidationManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** {@inheritDoc}      * @throws XMLStreamException */
specifier|public
name|void
name|setupValidation
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|utils
operator|.
name|setupValidation
argument_list|(
name|reader
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setupValidation
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|utils
operator|.
name|setupValidation
argument_list|(
name|writer
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

