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
name|common
operator|.
name|logging
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

begin_comment
comment|/**  * Simple replacement class for CXF logging utility.  * When deployed in OSGi, projects such as pax-logging can be used to redirect easily  * JUL to another framework such as log4j.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|LogUtils
block|{
specifier|private
name|LogUtils
parameter_list|()
block|{ }
specifier|public
specifier|static
name|Logger
name|getL7dLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|Logger
operator|.
name|getLogger
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
comment|//NOPMD
block|}
block|}
end_class

end_unit

