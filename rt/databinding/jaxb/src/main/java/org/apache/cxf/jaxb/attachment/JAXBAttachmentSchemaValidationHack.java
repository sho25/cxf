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
name|jaxb
operator|.
name|attachment
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|attachment
operator|.
name|AttachmentDataSource
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
name|helpers
operator|.
name|CastUtils
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
name|helpers
operator|.
name|ServiceUtils
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
name|interceptor
operator|.
name|Fault
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
name|Attachment
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JAXBAttachmentSchemaValidationHack
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|JAXBAttachmentSchemaValidationHack
name|INSTANCE
init|=
operator|new
name|JAXBAttachmentSchemaValidationHack
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SAVED_DATASOURCES
init|=
name|JAXBAttachmentSchemaValidationHack
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".SAVED_DATASOURCES"
decl_stmt|;
specifier|private
name|JAXBAttachmentSchemaValidationHack
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
comment|// This assumes that this interceptor is only use in IN / IN Fault chains.
if|if
condition|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|message
argument_list|)
operator|&&
name|message
operator|.
name|getAttachments
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|AttachmentDataSource
argument_list|>
name|dss
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Attachment
name|at
range|:
name|message
operator|.
name|getAttachments
argument_list|()
control|)
block|{
if|if
condition|(
name|at
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getDataSource
argument_list|()
operator|instanceof
name|AttachmentDataSource
condition|)
block|{
name|AttachmentDataSource
name|ds
init|=
operator|(
name|AttachmentDataSource
operator|)
name|at
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getDataSource
argument_list|()
decl_stmt|;
try|try
block|{
name|ds
operator|.
name|hold
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|dss
operator|.
name|add
argument_list|(
name|ds
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|dss
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SAVED_DATASOURCES
argument_list|,
name|dss
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|EndingInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|static
class|class
name|EndingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|static
specifier|final
name|EndingInterceptor
name|INSTANCE
init|=
operator|new
name|EndingInterceptor
argument_list|()
decl_stmt|;
name|EndingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Collection
argument_list|<
name|AttachmentDataSource
argument_list|>
name|dss
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|SAVED_DATASOURCES
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|AttachmentDataSource
name|ds
range|:
name|dss
control|)
block|{
name|ds
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

