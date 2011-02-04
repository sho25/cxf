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
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|client
operator|.
name|ui
operator|.
name|browser
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
name|annotation
operator|.
name|Nonnull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|dom
operator|.
name|client
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyDownEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyDownHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Composite
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|FlexTable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|FlowPanel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|FocusPanel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|HTMLTable
operator|.
name|Cell
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Label
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|ScrollPanel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|UIObject
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
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|client
operator|.
name|ui
operator|.
name|resources
operator|.
name|LogBrowserResources
import|;
end_import

begin_class
specifier|public
class|class
name|SelectableTable
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Composite
block|{
annotation|@
name|Nonnull
specifier|private
specifier|final
name|ScrollPanel
name|scroller
decl_stmt|;
annotation|@
name|Nullable
specifier|private
specifier|final
name|FocusPanel
name|focuser
decl_stmt|;
annotation|@
name|Nonnull
specifier|private
specifier|final
name|FlexTable
name|table
decl_stmt|;
annotation|@
name|Nonnull
specifier|private
specifier|final
name|List
argument_list|<
name|SelectRowHandler
argument_list|>
name|selectRowHandlers
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|hotkeysEnabled
decl_stmt|;
specifier|private
name|int
name|selectedRowIndex
decl_stmt|;
specifier|private
name|boolean
name|isRowSelected
decl_stmt|;
specifier|private
name|Label
name|messageLabel
decl_stmt|;
annotation|@
name|Nullable
specifier|private
name|List
argument_list|<
name|ColumnDefinition
argument_list|<
name|T
argument_list|>
argument_list|>
name|columnDefinitions
decl_stmt|;
annotation|@
name|Nonnull
specifier|private
name|LogBrowserResources
name|resources
init|=
name|GWT
operator|.
name|create
argument_list|(
name|LogBrowserResources
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|SelectableTable
parameter_list|(
specifier|final
name|boolean
name|hotkeysEnabled
parameter_list|)
block|{
name|this
operator|.
name|hotkeysEnabled
operator|=
name|hotkeysEnabled
expr_stmt|;
name|selectRowHandlers
operator|=
operator|new
name|ArrayList
argument_list|<
name|SelectRowHandler
argument_list|>
argument_list|()
expr_stmt|;
name|table
operator|=
operator|new
name|FlexTable
argument_list|()
expr_stmt|;
name|table
operator|.
name|setCellPadding
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|table
operator|.
name|setCellSpacing
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|table
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|messageLabel
operator|=
operator|new
name|Label
argument_list|()
expr_stmt|;
name|messageLabel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|FlowPanel
name|content
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|content
operator|.
name|add
argument_list|(
name|messageLabel
argument_list|)
expr_stmt|;
name|content
operator|.
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|scroller
operator|=
operator|new
name|ScrollPanel
argument_list|()
expr_stmt|;
if|if
condition|(
name|hotkeysEnabled
condition|)
block|{
name|focuser
operator|=
operator|new
name|FocusPanel
argument_list|()
expr_stmt|;
name|focuser
operator|.
name|setWidth
argument_list|(
literal|"99%"
argument_list|)
expr_stmt|;
name|focuser
operator|.
name|add
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|scroller
operator|.
name|add
argument_list|(
name|focuser
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|focuser
operator|=
literal|null
expr_stmt|;
name|scroller
operator|.
name|add
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
name|addEventHandlers
argument_list|()
expr_stmt|;
name|initWidget
argument_list|(
name|scroller
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setMessageInsteadOfData
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|String
name|message
parameter_list|,
annotation|@
name|Nullable
specifier|final
name|String
name|styleName
parameter_list|)
block|{
name|messageLabel
operator|.
name|setText
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|messageLabel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|table
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|styleName
operator|!=
literal|null
condition|)
block|{
name|messageLabel
operator|.
name|setStyleName
argument_list|(
name|styleName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setData
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|entries
parameter_list|)
block|{
assert|assert
name|columnDefinitions
operator|!=
literal|null
assert|;
name|table
operator|.
name|removeAllRows
argument_list|()
expr_stmt|;
name|messageLabel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|table
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|entries
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|T
name|entry
init|=
name|entries
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|columnDefinitions
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|ColumnDefinition
argument_list|<
name|T
argument_list|>
name|columnDefinition
init|=
name|columnDefinitions
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|i
argument_list|,
name|j
argument_list|,
name|columnDefinition
operator|.
name|getContent
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|restoreRowSelection
argument_list|()
expr_stmt|;
if|if
condition|(
name|hotkeysEnabled
condition|)
block|{
name|focuser
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setColumnDefinitions
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|List
argument_list|<
name|ColumnDefinition
argument_list|<
name|T
argument_list|>
argument_list|>
name|columnDefinitions
parameter_list|)
block|{
name|this
operator|.
name|columnDefinitions
operator|=
name|columnDefinitions
expr_stmt|;
name|setColumnsWidth
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setColumnDefinitions
parameter_list|(
name|ColumnDefinition
argument_list|<
name|T
argument_list|>
modifier|...
name|columnDefinitions
parameter_list|)
block|{
name|this
operator|.
name|columnDefinitions
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|columnDefinitions
argument_list|)
expr_stmt|;
name|setColumnsWidth
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addSelectRowHandler
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|SelectRowHandler
name|selectRowHandler
parameter_list|)
block|{
name|selectRowHandlers
operator|.
name|add
argument_list|(
name|selectRowHandler
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deselect
parameter_list|()
block|{
if|if
condition|(
name|table
operator|.
name|getRowCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|styleRow
argument_list|(
name|selectedRowIndex
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|selectedRowIndex
operator|=
literal|0
expr_stmt|;
name|isRowSelected
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addEventHandlers
parameter_list|()
block|{
name|table
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
specifier|public
name|void
name|onClick
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
name|performClickAction
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|hotkeysEnabled
condition|)
block|{
name|focuser
operator|.
name|addKeyDownHandler
argument_list|(
operator|new
name|KeyDownHandler
argument_list|()
block|{
specifier|public
name|void
name|onKeyDown
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|KeyDownEvent
name|event
parameter_list|)
block|{
name|performKeyDownAction
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|performClickAction
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
name|Cell
name|cell
init|=
name|table
operator|.
name|getCellForEvent
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|cell
operator|!=
literal|null
condition|)
block|{
name|int
name|row
init|=
name|cell
operator|.
name|getRowIndex
argument_list|()
decl_stmt|;
name|selectRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|performKeyDownAction
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|KeyDownEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|isRowSelected
condition|)
block|{
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
if|if
condition|(
name|event
operator|.
name|isUpArrow
argument_list|()
condition|)
block|{
name|selectRow
argument_list|(
name|selectedRowIndex
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|isDownArrow
argument_list|()
condition|)
block|{
name|selectRow
argument_list|(
name|selectedRowIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|ScrollMarker
name|scrollMarker
init|=
operator|new
name|ScrollMarker
argument_list|(
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|getElement
argument_list|(
name|selectedRowIndex
argument_list|)
argument_list|)
decl_stmt|;
name|scroller
operator|.
name|ensureVisible
argument_list|(
name|scrollMarker
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|selectRow
parameter_list|(
specifier|final
name|int
name|row
parameter_list|)
block|{
if|if
condition|(
name|row
operator|>=
literal|0
operator|&&
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|)
block|{
if|if
condition|(
name|isRowSelected
condition|)
block|{
name|styleRow
argument_list|(
name|selectedRowIndex
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|selectedRowIndex
operator|=
name|row
expr_stmt|;
name|styleRow
argument_list|(
name|selectedRowIndex
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|isRowSelected
operator|=
literal|true
expr_stmt|;
name|fireSelectRowEvent
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|styleRow
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|boolean
name|selected
parameter_list|)
block|{
name|String
name|style
init|=
name|resources
operator|.
name|css
argument_list|()
operator|.
name|browserTabSelectedRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|selected
condition|)
block|{
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|removeStyleName
argument_list|(
name|row
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|fireSelectRowEvent
parameter_list|()
block|{
for|for
control|(
name|SelectRowHandler
name|selectRowHandler
range|:
name|selectRowHandlers
control|)
block|{
name|selectRowHandler
operator|.
name|onSelectRow
argument_list|(
name|selectedRowIndex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|restoreRowSelection
parameter_list|()
block|{
if|if
condition|(
name|isRowSelected
operator|&&
name|selectedRowIndex
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|)
block|{
name|selectRow
argument_list|(
name|selectedRowIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|isRowSelected
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setColumnsWidth
parameter_list|()
block|{
assert|assert
name|columnDefinitions
operator|!=
literal|null
assert|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|columnDefinitions
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|ColumnDefinition
name|columnDefinition
init|=
name|columnDefinitions
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|table
operator|.
name|getColumnFormatter
argument_list|()
operator|.
name|setWidth
argument_list|(
name|j
argument_list|,
name|columnDefinition
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|ScrollMarker
extends|extends
name|UIObject
block|{
specifier|public
name|ScrollMarker
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|setElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
interface|interface
name|SelectRowHandler
block|{
name|void
name|onSelectRow
parameter_list|(
name|int
name|row
parameter_list|)
function_decl|;
block|}
specifier|public
specifier|static
interface|interface
name|ColumnDefinition
parameter_list|<
name|T
parameter_list|>
block|{
name|String
name|getContent
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
name|String
name|getWidth
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

