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
name|staxutils
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|endpoint
operator|.
name|Endpoint
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
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WoodstoxValidationImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Stax2ValidationUtils
name|utils
decl_stmt|;
specifier|public
name|WoodstoxValidationImpl
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
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Problem initializing MSV validation"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
specifier|public
name|boolean
name|canValidate
parameter_list|()
block|{
return|return
name|utils
operator|!=
literal|null
return|;
block|}
specifier|public
name|void
name|setupValidation
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|utils
operator|!=
literal|null
operator|&&
name|reader
operator|!=
literal|null
operator|&&
operator|!
name|utils
operator|.
name|setupValidation
argument_list|(
name|reader
argument_list|,
name|endpoint
argument_list|,
name|serviceInfo
argument_list|)
condition|)
block|{
name|utils
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setupValidation
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|utils
operator|!=
literal|null
operator|&&
name|writer
operator|!=
literal|null
operator|&&
operator|!
name|utils
operator|.
name|setupValidation
argument_list|(
name|writer
argument_list|,
name|endpoint
argument_list|,
name|serviceInfo
argument_list|)
condition|)
block|{
name|utils
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

