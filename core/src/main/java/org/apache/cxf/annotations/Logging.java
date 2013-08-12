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
name|annotations
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Enables message Logging  */
end_comment

begin_annotation_defn
annotation|@
name|Documented
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|}
argument_list|)
specifier|public
annotation_defn|@interface
name|Logging
block|{
comment|/**      * The size limit at which messages are truncated in the log       */
name|int
name|limit
parameter_list|()
default|default
literal|65536
function_decl|;
comment|/**      * the locations where the messages are logged.   The default is      *<logger> which means to log to the java.util.logging.Logger,       * but<stdout>,<stderr>, and a "file:/.." URI are acceptable.       */
name|String
name|inLocation
parameter_list|()
default|default
literal|"<logger>"
function_decl|;
name|String
name|outLocation
parameter_list|()
default|default
literal|"<logger>"
function_decl|;
comment|/**      * For XML content, turn on pretty printing in the logs      */
name|boolean
name|pretty
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Ignore binary payloads by default       */
name|boolean
name|showBinary
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

