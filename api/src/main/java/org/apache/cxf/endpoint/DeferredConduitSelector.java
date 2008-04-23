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
name|endpoint
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
name|Logger
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
name|transport
operator|.
name|Conduit
import|;
end_import

begin_comment
comment|/**  * Strategy for lazy deferred retreival of a Conduit to mediate an   * outbound message.  */
end_comment

begin_class
specifier|public
class|class
name|DeferredConduitSelector
extends|extends
name|AbstractConduitSelector
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
name|DeferredConduitSelector
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Normal constructor.      */
specifier|public
name|DeferredConduitSelector
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor, allowing a specific conduit to override normal selection.      *       * @param c specific conduit      */
specifier|public
name|DeferredConduitSelector
parameter_list|(
name|Conduit
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called prior to the interceptor chain being traversed.      *       * @param message the current Message      */
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|// nothing to do
block|}
comment|/**      * Called when a Conduit is actually required.      *       * @param message      * @return the Conduit to use for mediation of the message      */
specifier|public
name|Conduit
name|selectConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getSelectedConduit
argument_list|(
name|message
argument_list|)
return|;
block|}
comment|/**      * @return the logger to use      */
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
end_class

end_unit

